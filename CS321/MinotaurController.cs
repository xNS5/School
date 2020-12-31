using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.AI;

public class MinotaurController : MonoBehaviour, IHittable
{
    [SerializeField] private float walkSpeed = 2f;
    [SerializeField] private int health = 200, attack = 80, strength = 40, defense = 100, speed = 4;
    [SerializeField] private GameObject sword, axe, rHand, lHand;
    private GameObject _player;
    private NavMeshAgent _agent;
    private Animator _anim;
    private MonsterHealth m_Hp;
    private Collider r, l, sword_collider, axe_collider;
    private int _attack_sequence = 0, target = 0;
    private bool sword_drawn = false, axe_drawn = false, in_range = false, right_enabled = true, left_enabled = true;

    // Start is called before the first frame update
    void Start()
    {
        _agent = GetComponent<NavMeshAgent>();
        _anim = GetComponent<Animator>();
        m_Hp = GetComponent<MonsterHealth>();
        _player = GameObject.FindWithTag("Player");
        r = rHand.GetComponent<Collider>();
        l = lHand.GetComponent<Collider>();
        sword_collider = sword.GetComponent<Collider>();
        axe_collider = axe.GetComponent<Collider>();
        m_Hp.SetMonsterHealth(health, attack, strength, defense, speed);
        _agent.speed = walkSpeed;
        target = health - 50;
        StartCoroutine(Idle());
    }

    // Update is called once per frame
    void Update()
    {
        if (m_Hp.GETHealth() <= 0)
        {
            _agent.ResetPath();
            StartCoroutine(Death());
        }

        if (m_Hp.GETHealth() <= target)
        {
            CalculateTarget();
            _attack_sequence += 1;
            _anim.SetInteger("AttackSequence", _attack_sequence);
        }

        if (right_enabled && _attack_sequence == 1)
        {
            right_enabled = false;
            r.isTrigger = false;
        }

        if (left_enabled && _attack_sequence == 2)
        {
            left_enabled = false;
            l.isTrigger = false;
        }
        if (!in_range)
        {
            _agent.isStopped = false;
            _agent.SetDestination(_player.transform.position);
        }
    }

    public void Hit(int damage)
    {
        m_Hp.SetHealth(damage);
        if (m_Hp.GETHealth() > 0)
        {
            StartCoroutine(TakeDamage());
        }
    }

    public void EnableSword()
    {
        sword.SetActive(true);
    }
    
    public void EnableAxe()
    {
        axe.SetActive(true);
    }

    private void CalculateTarget()
    {
        target -= 50;
    }

    private void OnTriggerEnter(Collider other)
    {
        if (other.CompareTag("Player"))
        {
            in_range = true;
            _agent.isStopped = true;
            if (_attack_sequence == 2 && !sword_drawn && in_range)
            {
                _anim.SetBool("Sword", true);
                StartCoroutine(DrawSword());
                _anim.SetBool("Sword", false);
                sword_drawn = true;
            }

            if (_attack_sequence == 3 && !axe_drawn && in_range)
            {
                _anim.SetBool("Axe", true);
                StartCoroutine(DrawAxe());
                _anim.SetBool("Axe", false);
                axe_drawn = true;
            }
            StartCoroutine(attack_sequence());
        }
    }

    private void OnTriggerExit(Collider other)
    {
        if (other.CompareTag("Player"))
        {
            in_range = false;
        }
    }
    
    private bool isAttack(String name)
    {
        String[] names = {"Double Punch","Double Fist Slam", "Draw Sword", "SwordSlash", "DrawAxe", "Sword+Axe"};
        foreach (String n in names)
        {
            if (n.Equals(name))
            {
                return true;
            }
        }
        return false;
    }

    private IEnumerator Idle()
    {
        Debug.Log("Idle");
        while (_anim.GetCurrentAnimatorStateInfo(0).IsName("Idle") || _anim.GetCurrentAnimatorStateInfo(0).IsName("Shout")  && _anim.GetCurrentAnimatorStateInfo(0).normalizedTime < 1f
        && !in_range)
        {
            yield return null;
        }
    }

    private IEnumerator attack_sequence()
    {
        Debug.Log("Attack");
        AnimatorClipInfo[] _curr = _anim.GetCurrentAnimatorClipInfo(0);
        _anim.SetBool("InRange", true);

        while (in_range && isAttack(_curr[0].clip.name))
        {
            _agent.isStopped = true;
            yield return null;
        }

        switch (_attack_sequence)
        {
            case 0:
                r.enabled = true;
                l.enabled = true;
                break;
            case 1:
                l.enabled = true;
                break;
            case 2:
                sword_collider.enabled = true;
                break;
            case 3:
                sword_collider.enabled = true;
                axe_collider.enabled = true;
                break;
        }
    }

    private IEnumerator DrawSword()
    {

        while (_anim.GetCurrentAnimatorStateInfo(0).IsName("DrawSword") &&
               _anim.GetCurrentAnimatorStateInfo(0).normalizedTime < 1.0f)
        {
            yield return null;
        }
        
    }

    private IEnumerator DrawAxe()
    {
        while (_anim.GetCurrentAnimatorStateInfo(0).IsName("DrawAxe") &&
               _anim.GetCurrentAnimatorStateInfo(0).normalizedTime < 1.0f)
        {
            yield return null;
        }
    }

    private IEnumerator TakeDamage()
    {
        _anim.SetTrigger("Hit");
        while (_anim.GetCurrentAnimatorStateInfo(0).IsName("Get_Hit") &&
               _anim.GetCurrentAnimatorStateInfo(0).normalizedTime < 1.0f)
        {
            _agent.isStopped = true;
            yield return null;
        }
        _anim.ResetTrigger("Hit");
        _agent.SetDestination(_player.transform.position);
    }
    
    private IEnumerator Death()
    {
        _anim.SetBool("Dead", true);
        yield return new WaitForSeconds(10);
        gameObject.SetActive(false);
    }
}
