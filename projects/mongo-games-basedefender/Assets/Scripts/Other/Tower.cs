using UnityEngine;

public class Tower : MonoBehaviour
{
    [SerializeField] private float _shootTimerMax;
    private Enemy _targetEnemy;
    private float _lookForTargetTimer;
    private float _lookForTargetTimerMax = .2f;
    private Vector3 _projectileSpawnPosition;
    private float _shootTimer;

    private void Awake()
    {
        _projectileSpawnPosition = transform.Find("projectileSpawnPosition").position;
    }

    private void Update()
    {
        HandleTargeting();
        HandleShooting();
    }

    private void HandleTargeting()
    {
        _lookForTargetTimer -= Time.deltaTime;
        if (_lookForTargetTimer < 0f)
        {
            _lookForTargetTimer += _lookForTargetTimerMax;
            LookForTargets();
        }
    }

    private void HandleShooting()
    {
        _shootTimer -= Time.deltaTime;

        if (_shootTimer <= 0f)
        {
            _shootTimer += _shootTimerMax;    
            if (_targetEnemy != null)
            {
                ArrowProjectile.Create(_projectileSpawnPosition, _targetEnemy);
            }
        }
    }

    private void LookForTargets()
    {
        float targetMaxRadius = 20f;
        Collider2D[] collider2DArray = Physics2D.OverlapCircleAll(transform.position, targetMaxRadius);

        foreach (Collider2D collider2D in collider2DArray)
        {
            Enemy enemy = collider2D.GetComponent<Enemy>();

            if (enemy != null)
            {
                if (_targetEnemy == null)
                {
                    _targetEnemy = enemy;
                }
                else
                {
                    if (Vector3.Distance(transform.position, enemy.transform.position) <
                        Vector3.Distance(transform.position, _targetEnemy.transform.position))
                    {
                        _targetEnemy = enemy;
                    }
                }
            }
        }
    }
}
