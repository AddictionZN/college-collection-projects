using UnityEngine;

public class HealthBar : MonoBehaviour
{
    [SerializeField] private HealthSystem _healthSystem;

    private Transform _barTransform;

    private void Awake()
    {
        _barTransform = transform.Find("bar");
    }

    private void Start()
    {
        _healthSystem.OnDamaged += HealthSystem_OnDamaged;
        _healthSystem.OnHeal += HealthSystem_OnHeal;
        UpdateBar();
        UpdateHealthBarVisible();
    }

    private void HealthSystem_OnHeal(object sender, System.EventArgs e)
    {
        UpdateBar();
        UpdateHealthBarVisible();
    }

    private void HealthSystem_OnDamaged(object sender, System.EventArgs e)
    {
        UpdateBar();
        UpdateHealthBarVisible();
    }

    private void UpdateBar()
    {
        _barTransform.localScale = new Vector3(_healthSystem.GetHealthAmountNormalized(), 1, 1);
    }

    private void UpdateHealthBarVisible()
    {
        if (_healthSystem.IsFullHealth())
        {
            gameObject.SetActive(false);
        }
        else
        {
            gameObject.SetActive(true);
        }
    }
}