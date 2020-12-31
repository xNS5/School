using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.AI;
using Random = UnityEngine.Random;

namespace Monster.Goblin
{
    public class GoblinController : MonoBehaviour
    {
        private NavMeshAgent _agent;
        [SerializeField] private Animator anim;
        [SerializeField] private Transform waypoints;
        [SerializeField] private float walkSpeed = 1;
        [SerializeField] private int runSpeed = 3;
        [SerializeField] private int index;
        [SerializeField] private float length = 10;
        [SerializeField] private int gHealth = 20, gAttack = 10, gStrength = 10, gDefense = 10, gSpeed = 3;
        [SerializeField] private int angle = 45, angleSegments = 10;
        [SerializeField] private AudioSource death;

        public MonsterHealth m_Hp;
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
            if (_traveling && _agent.remainingDistance <= 1f)
            {
                _traveling = false;
                ChangeDest();
                Dest();
            }

            if (_traveling && _agent.remainingDistance <= 1f)
            {
                _traveling = false;
                _agent.isStopped = true;
            } 

            if (m_Hp.GETHealth() == 0)
            {
                _agent.isStopped = true;
                _traveling = false;
                StartCoroutine(Death(5));
            }
        }

        private void FixedUpdate()
        {
            // Modified raycaster to scan area instead of a single point.
            RaycastHit hit;
            Vector3 origin = transform.position + transform.up; // Adjusting the height of the raycast
            Vector3 target = Vector3.zero;
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
                transform.LookAt(other.transform);
            }
        }

        private void OnTriggerExit(Collider other)
        {
            if (other.CompareTag("Player"))
            {
                _traveling = true;
                _agent.isStopped = false;
                transform.LookAt(other.transform);
            }
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
            yield return new WaitForSeconds(seconds);
            gameObject.SetActive(false);

        }
        

    }
}
