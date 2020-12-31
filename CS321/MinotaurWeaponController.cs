using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using Random = UnityEngine.Random;


public class MinotaurWeaponController : MonoBehaviour
{

    private GameObject monster;
    private GameObject player;
    private MonsterHealth health_script;
    private Collider trigger;
    private int maxDamage;

    void Start()
    {
        monster = transform.root.gameObject; 
        health_script = monster.GetComponent<MonsterHealth>();
        trigger = GetComponent<Collider>();
        float[] stats = health_script.GETAllStats();
        foreach (float stat in stats)
        {
            maxDamage += (int)stat;
        }
        maxDamage = CalculateDamage();
    }

    private void OnTriggerEnter(Collider other)
    {
        if (other.CompareTag("Player"))
        {
            if (player == null)
            {
                player = other.gameObject;
            }
            bool blocking = player.GetComponent<PlayerController>().IsBlocking();
            if ( !blocking || blocking && Random.Range(0, 10) % 2 != 0)
            {
                Debug.Log("Hit!");
                player.GetComponent<PlayerController>().Hit(Random.Range(0, CalculateDamage()));
            }
            else
            {
                Debug.Log("Blocked!");
            }
        }
    }

    private void OnTriggerExit(Collider other)
    {
        if (other.CompareTag("Player"))
        {
            GetComponent<Collider>().enabled = false;
        }
    }

    private int CalculateDamage()
    {
        return Convert.ToInt32(maxDamage * .08);
    }
}