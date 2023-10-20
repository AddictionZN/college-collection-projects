using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;

public class EnemyWaveUI : MonoBehaviour
{
    [SerializeField] private EnemyWaveManager _enemyWaveManager;

    private TextMeshProUGUI _waveNumberText;
    private TextMeshProUGUI _waveMessageText;
    private RectTransform _enemyWaveSpawnPositionIndicator;
    private RectTransform _enemyClosestPositionIndicator;
    private Camera _mainCamera;

    private void Awake()
    {
        _waveNumberText = transform.Find("waveNumberText").GetComponent<TextMeshProUGUI>();
        _waveMessageText = transform.Find("waveMessageText").GetComponent<TextMeshProUGUI>();
        _enemyWaveSpawnPositionIndicator = transform.Find("enemyWaveSpawnPositionIndicator").GetComponent<RectTransform>();
        _enemyClosestPositionIndicator = transform.Find("enemyClosestPositionIndicator").GetComponent<RectTransform>();
    }

    private void Update()
    {
        HandleNextWaveMessage();
        HandleEnemyWaveSpawnPositionIndicator();
        HandleEnemyClosestPositionIndicator();
    }

    private void Start()
    {
        _mainCamera = Camera.main;
        _enemyWaveManager.OnWaveNumberChanged += EnemyWaveManager_OnWaveNumberChanged;
    }

    private void HandleNextWaveMessage()
    {
        float nextWaveSpawnTimer = _enemyWaveManager.GetNextWaveSpawnTimer();
        if (nextWaveSpawnTimer <= 0f)
        {
            SetMessageText("");
        }
        else
        {
            SetMessageText("Next Wave in " + nextWaveSpawnTimer.ToString("F1") + "s");
        }
    }

    private void HandleEnemyClosestPositionIndicator()
    {
        float targetMaxRadius = 99999f;
        Collider2D[] collider2DArray = Physics2D.OverlapCircleAll(_mainCamera.transform.position, targetMaxRadius);

        Enemy targetEnemy = null;

        foreach (Collider2D collider2D in collider2DArray)
        {
            Enemy enemy = collider2D.GetComponent<Enemy>();

            if (enemy != null)
            {
                if (targetEnemy == null)
                {
                    targetEnemy = enemy;
                }
                else
                {
                    if (Vector3.Distance(transform.position, enemy.transform.position) <
                        Vector3.Distance(transform.position, targetEnemy.transform.position))
                    {
                        targetEnemy = enemy;
                    }
                }
            }
        }

        if (targetEnemy != null)
        {
            Vector3 dirToClosestEnemy = (targetEnemy.transform.position - _mainCamera.transform.position).normalized;

            _enemyClosestPositionIndicator.anchoredPosition = dirToClosestEnemy * 250f;
            _enemyClosestPositionIndicator.eulerAngles = new Vector3(0, 0, UtilsClass.GetAngleFromVector(dirToClosestEnemy));

            float distanceToClosestEnemy = Vector3.Distance(_enemyWaveManager.GetSpawnPosition(), _mainCamera.transform.position);
            _enemyClosestPositionIndicator.gameObject.SetActive(distanceToClosestEnemy > _mainCamera.orthographicSize * 1.5f);
        }
        else
        {
            _enemyClosestPositionIndicator.gameObject.SetActive(false);
        }
    }

    private void HandleEnemyWaveSpawnPositionIndicator()
    {
        Vector3 dirToNextSpawnPosition = (_enemyWaveManager.GetSpawnPosition() - _mainCamera.transform.position).normalized;

        _enemyWaveSpawnPositionIndicator.anchoredPosition = dirToNextSpawnPosition * 300f;
        _enemyWaveSpawnPositionIndicator.eulerAngles = new Vector3(0, 0, UtilsClass.GetAngleFromVector(dirToNextSpawnPosition));

        float distanceToNextSpawnPosition = Vector3.Distance(_enemyWaveManager.GetSpawnPosition(), _mainCamera.transform.position);
        _enemyWaveSpawnPositionIndicator.gameObject.SetActive(distanceToNextSpawnPosition > _mainCamera.orthographicSize * 1.5f);
    }

    private void EnemyWaveManager_OnWaveNumberChanged(object sender, System.EventArgs e)
    {
        SetWaveText("Wave " + _enemyWaveManager.GetWaveNumber());
    }

    private void SetMessageText(string message)
    {
        _waveMessageText.SetText(message);
    }

    private void SetWaveText(string text)
    {
        _waveNumberText.SetText(text);
    }
}
