using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.AI;
using Random = UnityEngine.Random;

namespace Monster.Golem
{
    public class GolemController : MonoBehaviour, IHittable
    {
        private NavMeshAgent _agent;
        [SerializeField] private Animator anim;
        [SerializeField] private Transform waypoints;
        [SerializeField] private float walkSpeed = 1;
        [SerializeField] private int runSpeed = 3;
        [SerializeField] private int index;
        [SerializeField] private float length = 20;
        [SerializeField] private int gHealth = 45, gAttack =20 , gStrength = 20, gDefense = 20, gSpeed = 3;
        [SerializeField] private int angle = 45, angleSegments = 10;
        [SerializeField] private AudioSource death;
        private MonsterHealth m_Hp;
        private bool _traveling;
 
        private List<Transform> patrolPoint = new List<Transform>();

        // Start is called before the first frame update
        void Start()
        {
            _agent = GetComponent<NavMeshAgent>();
            anim = GetComponent<Animator>();
            m_Hp = GetComponent<MonsterHealth>();
            m_Hp.SetMonsterHealth(gHealth, gAttack, gStrength, gDefense, gSpeed);
            _agent.speed = walkSpeed;
            if (waypoints != null)
            {
                index = 0;
                foreach (Transform child in waypoints)
                {
                    patrolPoint.Add(child);
                }
                Dest();
            }
        }

        // Update is called once per frame
        void Update()
        {
            if ( Mathf.Abs(_agent.velocity.x) > 0.1 || Mathf.Abs(_agent.velocity.z) > 0.1)
            {
                anim.SetBool("Walking", true);
            }
            else
            {
                anim.SetBool("Walking", false);
            }
            if (_traveling && _agent.remainingDistance <= 2f)
            {
                _traveling = false;
                ChangeDest();
                Dest();
            }
            
            if (m_Hp.GETHealth() == 0)
            {
                StartCoroutine(Death(10));
            }
        }

        public void Hit(int damage)
        {
            m_Hp.SetHealth(damage);
            
        }
        private void FixedUpdate()
        {
            // Modified raycaster to scan area instead of a single point.
            RaycastHit hit;
            Vector3 origin = transform.position; // Adjusting the height of the raycast
            Vector3 target = Vector3.zero;
            int startAngle = Convert.ToInt32(-angle * 0.5), endAngle = Convert.ToInt32(angle * 0.5);
            int increments = Convert.ToInt32(angle / angleSegments);
            for (int i = startAngle; i < endAngle; i += increments)
            {
                target = (Quaternion.Euler(0, i, 0) * transform.forward).normalized;
                if( Physics.Raycast(origin, transform.TransformDirection(target) * length, out hit, length) && hit.collider.CompareTag("Player"))
                {
                    if (!hit.collider.GetComponent<PlayerController>().IsSneaking())
                    {
                        var hitPos = hit.transform.position;
                        Debug.DrawRay(origin, transform.TransformDirection(Vector3.forward) * length, Color.red);
                        transform.LookAt(hitPos);
                        _agent.speed = runSpeed;
                        anim.SetTrigger("Spotted");
                        _agent.SetDestination(new Vector3(hitPos.x, hitPos.y, hitPos.z - 2));
                    }
                }
                else
                {
                    Debug.DrawRay(origin, transform.TransformDirection(Vector3.forward) * length, Color.red);
                }
            }
        }

        private void OnTriggerEnter(Collider other)
        {
            if (other.CompareTag("Player"))
            {
                transform.LookAt(other.transform);
                _traveling = false;
                _agent.isStopped = true;
                anim.SetBool("InRange", true);
            }
        }

        private void OnTriggerStay(Collider other)
        {
            if (other.CompareTag("Player"))
            {
                StartCoroutine(Wait(5));
                transform.LookAt(other.transform);
            }
        }

        private void OnTriggerExit(Collider other)
        {
            if (other.CompareTag("Player"))
            {
                anim.SetBool("InRange", false);
                anim.SetBool("Spotted", false);
                _traveling = true;
                _agent.isStopped = false;
            }
        }

        private static IEnumerator Wait(int time)
        {
            yield return new WaitForSeconds(time);
        }

        private void Dest()
        {
            if (patrolPoint != null)
            {
                _agent.speed = walkSpeed;
                Vector3 target = patrolPoint[index].transform.position;
                _agent.SetDestination(target);
                _traveling = true;
            }
        }

        private void ChangeDest()
        {
            index = Random.Range(0, patrolPoint.Count);
        }
        
        private IEnumerator Death(int seconds)
        {

            anim.SetBool("Dead", true);
            death.Play();
            _agent.isStopped = true;
            yield return new WaitForSeconds(seconds);
            gameObject.SetActive(false);
        }

    }
}
