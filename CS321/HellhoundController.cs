using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.AI;
using Random = UnityEngine.Random;

namespace Monster.Hellhound
{
    public class HellhoundController : MonoBehaviour, IHittable
    {
        private NavMeshAgent _agent;
        [SerializeField] private Animator anim;
        [SerializeField] private Transform waypoints;
        [SerializeField] private int index;
        [SerializeField] private float walkSpeed = 2, runSpeed = 10, length = 10;
        [SerializeField] private int hHealth = 50, hAttack = 40, hStrength = 15, hDefense = 20, hSpeed = 10;
        [SerializeField] private int angle = 45, angleSegments = 10;
        [SerializeField] private AudioSource death;
        private MonsterHealth m_Hp;
        private bool _traveling, spotted, walking;

        private List<Transform> patrolPoint = new List<Transform>();

        // Start is called before the first frame update
        void Start()
        {
            _agent = GetComponent<NavMeshAgent>();
            anim = GetComponent<Animator>();
            _agent.speed = walkSpeed;
            m_Hp = GetComponent<MonsterHealth>();
            m_Hp.SetMonsterHealth(hHealth, hAttack, hStrength, hDefense, hSpeed);
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
        
            if (_traveling && _agent.remainingDistance <= 1f)
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

        private void FixedUpdate()
        {
            // Modified raycaster to scan area instead of a single point.
            RaycastHit hit;
            var origin = transform.position + transform.up; // Adjusting the height of the raycast
            Vector3 target;
            int startAngle = Convert.ToInt32(-angle * 0.5), endAngle = Convert.ToInt32(angle * 0.5);
            int increments = Convert.ToInt32(angle / angleSegments);
            for (int i = startAngle; i < endAngle; i += increments)
            {
                target = (Quaternion.Euler(0, i, 0) * transform.forward).normalized;
                if( Physics.Raycast(origin, transform.TransformDirection(target) * length, out hit, length) && hit.collider.CompareTag("Player"))
                {
                    var hitPos = hit.transform.position;
                    Debug.DrawRay(origin, transform.TransformDirection(Vector3.forward) * length, Color.red);
                    transform.LookAt(hitPos);
                    _agent.speed = runSpeed;
                    anim.SetBool("Spotted", true);
                    _agent.SetDestination(new Vector3(hitPos.x, hitPos.y,hitPos.z-2));
                }
                else
                {
                    Debug.DrawRay(origin, transform.TransformDirection(Vector3.forward) * length, Color.red);
                }
            }
        }

        public void Hit(int damage) {
            m_Hp.SetHealth(damage);
            StartCoroutine(Hit());
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

        private void OnTriggerExit(Collider other)
        {
            if (other.CompareTag("Player"))
            {
                _traveling = true;
                _agent.isStopped = false;
                transform.LookAt(other.transform);
                anim.SetBool("InRange", false);
            }
        }

        private void Dest()
        {
            if (patrolPoint != null)
            {
                Vector3 target = patrolPoint[index].transform.position;
                Quaternion.RotateTowards(transform.rotation, Quaternion.LookRotation(target), Time.deltaTime * 5f);
                _agent.SetDestination(target);
                _traveling = true;
            }
        }

        private void ChangeDest()
        {
            index = Random.Range(0, patrolPoint.Count);
        }

        private IEnumerator Hit()
        {
            _agent.isStopped = true;
            anim.SetTrigger("Hit");
            while (anim.GetCurrentAnimatorStateInfo(0).IsName("NordicWolfHit") &&
                   anim.GetCurrentAnimatorStateInfo(0).normalizedTime < 1.0f)
            {
                yield return null;
            }
            anim.ResetTrigger("Hit");
            _agent.isStopped = false;
        }
    
        private IEnumerator Death(int seconds)
        {
            _agent.isStopped = true;
            _agent.ResetPath();
            anim.SetBool("Dead", true);
            death.Play();
            yield return new WaitForSeconds(seconds);
            gameObject.SetActive(false);
        }

    }
}
