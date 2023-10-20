using UnityEngine;

public class ArrowProjectile : MonoBehaviour
{
    private Enemy _targetEnemy;
    private Vector3 _lastMoveDir;
    private float _timeToDie = 2f;

    public static ArrowProjectile Create(Vector3 position, Enemy enemy)
    {
        Transform pfArrowProjectile = Resources.Load<Transform>("pfArrowProjectile");
        Transform arrowTransform = Instantiate(pfArrowProjectile, position, Quaternion.identity);

        ArrowProjectile arrowProjectile = arrowTransform.GetComponent<ArrowProjectile>();

        arrowProjectile.SetTarget(enemy);

        return arrowProjectile;
    }

    private void Update()
    {
        Vector3 moveDir;
        float moveSpeed = 20f;

        if (_targetEnemy != null)
        {
            moveDir = (_targetEnemy.transform.position - transform.position).normalized;
            _lastMoveDir = moveDir;
        }
        else
        {
            moveDir = _lastMoveDir;
        }

        transform.position += moveDir * moveSpeed * Time.deltaTime;

        transform.eulerAngles = new Vector3(0, 0, UtilsClass.GetAngleFromVector(moveDir));

        _timeToDie -= Time.deltaTime;
        if (_timeToDie < 0f)
        {
            Destroy(gameObject);
        }
    }

    private void SetTarget(Enemy targetEnemy)
    {
        _targetEnemy = targetEnemy;
    }

    private void OnTriggerEnter2D(Collider2D collision)
    {
        Enemy enemy = collision.GetComponent<Enemy>();

        if (enemy != null)
        {
            int damageAmount = 10;
            enemy.GetComponent<HealthSystem>().Damage(damageAmount);
            Destroy(gameObject);
        }
    }
}