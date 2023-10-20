using System;
using System.Collections.Generic;
using UnityEngine;

public class EnemyWaveManager : MonoBehaviour
{
    public event EventHandler OnWaveNumberChanged;
    public static EnemyWaveManager Instance { get; private set; }

    private enum State
    {
        WaitingToSpawnNextWave,
        SpawningWave
    }

    [SerializeField] private List<Transform> _spawnPositionTransformList;
    [SerializeField] private Transform _nextWaveSpawnPositionTransform;


    private State _state;
    private float _nextWaveSpawnTimer;
    private float _nextEnemySpawnTimer;
    private int _remainingEnemySpawnAmount;
    private Vector3 _spawnPosition;
    private int _waveNumber;

    private void Awake()
    {
        Instance = this;
    }

    private void Start()
    {
        _state = State.WaitingToSpawnNextWave;
        _spawnPosition = _spawnPositionTransformList[UnityEngine.Random.Range(0, _spawnPositionTransformList.Count)].position;
        _nextWaveSpawnPositionTransform.position = _spawnPosition;
        _nextWaveSpawnTimer = 3f;
    }

    private void Update()
    {
        switch (_state)
        {
            case State.WaitingToSpawnNextWave:
                _nextWaveSpawnTimer -= Time.deltaTime;
                if (_nextWaveSpawnTimer < 0f) SpawnWave();
                break;
            case State.SpawningWave:
                if (_remainingEnemySpawnAmount > 0)
                {
                    _nextEnemySpawnTimer -= Time.deltaTime;
                    if (_nextEnemySpawnTimer < 0f)
                    {
                        _nextEnemySpawnTimer = UnityEngine.Random.Range(0f, .2f);
                        Enemy.Create(_spawnPosition + UtilsClass.GetRandomDir() * UnityEngine.Random.Range(0f, 10f));
                        _remainingEnemySpawnAmount--;
                    }
                }

                if (_remainingEnemySpawnAmount <= 0)
                {
                    _state = State.WaitingToSpawnNextWave;
                    _spawnPosition = _spawnPositionTransformList[UnityEngine.Random.Range(0, _spawnPositionTransformList.Count)].position;
                    _nextWaveSpawnPositionTransform.position = _spawnPosition;
                }
                break;
        }
    }

    private void SpawnWave()
    {
        _nextWaveSpawnTimer = 15f;
        _remainingEnemySpawnAmount = 5 + 3 * _waveNumber;
        _state = State.SpawningWave;
        _waveNumber++;
        OnWaveNumberChanged?.Invoke(this, EventArgs.Empty);
    }

    public int GetWaveNumber()
    {
        return _waveNumber;
    }

    public float GetNextWaveSpawnTimer()
    {
        return _nextWaveSpawnTimer;
    }

    public Vector3 GetSpawnPosition()
    {
        return _spawnPosition;
    }
}
