using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SocialPlatforms;
using Random = UnityEngine.Random;


public class WeaponController : MonoBehaviour
{

    public GameObject monster;
    private GameObject player;
    private MonsterHealth health_script;
    private PlayerController pc;
    private float health, attack, strength, defense, speed;
    private int maxDamage;

    void Start()
    {
        Debug.Log(monster.name);
        health_script = monster.GetComponent<MonsterHealth>();
        float[] stats = health_script.GETAllStats();
        health = stats[0];
        attack = stats[1];
        strength = stats[2];
        defense = stats[3];
        speed = stats[4];
        maxDamage = CalculateDamage();
    }

    private void OnTriggerEnter(Collider other)
    {
        if (other.CompareTag("Player"))
        {
            if (player == null)
            {
                player = other.gameObject;
                pc = player.GetComponent<PlayerController>();
            }
            if (!pc.IsBlocking() || pc.IsBlocking() && Random.Range(0, 5) % 2 != 0)
            {
                int damage = Random.Range(0, maxDamage);
                Debug.Log(damage);
                pc.Hit(Random.Range(0, damage));
            }
        }
    }

    private int CalculateDamage()
    {
        return Convert.ToInt32(health * 0.2 + attack * 0.2 + strength * 0.3 + defense * 0.2 + speed * 0.8);
    }
}
