using UnityEngine;

public class Enemy : MonoBehaviour
{
    private Transform _targetTransform;
    private Rigidbody2D _rigidbody2D;
    private float _lookForTargetTimer;
    private float _lookForTargetTimerMax = .2f;
    private HealthSystem _healthSystem;

    public static Enemy Create(Vector3 position)
    {
        Transform pfEnemy = Resources.Load<Transform>("pfEnemy");
        Transform enemyTransform = Instantiate(pfEnemy, position, Quaternion.identity);

        Enemy enemy = enemyTransform.GetComponent<Enemy>();
        return enemy;
    }

    private void Start()
    {
        _rigidbody2D = GetComponent<Rigidbody2D>();

        if (BuildingManager.Instance.GetHQBuilding() != null)
        {
            _targetTransform = BuildingManager.Instance.GetHQBuilding().transform;
        }
        
        _healthSystem = GetComponent<HealthSystem>();
        _healthSystem.OnDied += HealthSystem_OnDied;
        _lookForTargetTimer = Random.Range(0f, _lookForTargetTimerMax);
    }

    private void Update()
    {
        HandleMovement();
        HandleTargeting();
    }

    private void OnCollisionEnter2D(Collision2D collision)
    {
        Building building = collision.gameObject.GetComponent<Building>();

        if (building != null)
        {
            HealthSystem healthSystem = building.GetComponent<HealthSystem>();
            healthSystem.Damage(10);
            Destroy(gameObject);
        }
    }

    private void HealthSystem_OnDied(object sender, System.EventArgs e)
    {
        Destroy(gameObject);
    }

    private void HandleMovement()
    {
        if (_targetTransform != null)
        {
            Vector3 moveDir = (_targetTransform.position - transform.position).normalized;

            float moveSpeed = 6f;
            _rigidbody2D.velocity = moveDir * moveSpeed;
        }
        else
        {
            _rigidbody2D.velocity = Vector2.zero;
        }
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

    private void LookForTargets()
    {
        float targetMaxRadius = 10f;
        Collider2D[] collider2DArray = Physics2D.OverlapCircleAll(transform.position, targetMaxRadius);

        foreach (Collider2D collider2D in collider2DArray)
        {
            Building building = collider2D.GetComponent<Building>();

            if (building != null)
            {
                if (_targetTransform == null)
                {
                    _targetTransform = building.transform;
                }
                else
                {
                    if (Vector3.Distance(transform.position, building.transform.position) < Vector3.Distance(transform.position, _targetTransform.position))
                    {
                        _targetTransform = building.transform;
                    }
                }
            }
        }

        if (_targetTransform == null)
        {
            if (BuildingManager.Instance.GetHQBuilding() != null)
            {
                _targetTransform = BuildingManager.Instance.GetHQBuilding().transform;
            }
        }
    }
}
