using System;
using UnityEngine;

public class HealthSystem : MonoBehaviour
{
    [SerializeField] private int _healthAmountMax;
    private int _healthAmount;

    public event EventHandler OnDamaged;
    public event EventHandler OnDied;
    public event EventHandler OnHeal;

    public void Awake()
    {
        _healthAmount = _healthAmountMax;
    }

    public void Damage(int damageAmount)
    {
        _healthAmount -= damageAmount;
        _healthAmount = Mathf.Clamp(_healthAmount, 0, _healthAmountMax);

        OnDamaged?.Invoke(this, EventArgs.Empty);

        if (IsDead())
        {
            OnDied?.Invoke(this, EventArgs.Empty);
        }
    }

    public void Heal(int healthAmount)
    {
        _healthAmount += healthAmount;
        _healthAmount = Mathf.Clamp(_healthAmount, 0, _healthAmountMax);

        OnHeal?.Invoke(this, EventArgs.Empty);
    }

    public void HealFull()
    {
        _healthAmount = _healthAmountMax;

        OnHeal?.Invoke(this, EventArgs.Empty);
    }

    public bool IsDead()
    {
        return _healthAmount == 0;
    }

    public int GetHealthAmount()
    {
        return _healthAmount;
    }
    
    public int GetHealthAmountMax()
    {
        return _healthAmountMax;
    }

    public float GetHealthAmountNormalized()
    {
        return (float)_healthAmount / _healthAmountMax;
    }

    public bool IsFullHealth()
    {
        return _healthAmount == _healthAmountMax;
    }

    public void SetHealthAmountMax(int healthAmountMax, bool updateHealthAmount)
    {
        _healthAmountMax = healthAmountMax;

        if (updateHealthAmount)
        {
            _healthAmount = healthAmountMax;
        }
    }
}
