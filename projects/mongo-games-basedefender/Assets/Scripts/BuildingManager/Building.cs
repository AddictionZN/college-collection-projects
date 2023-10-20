using UnityEngine;

public class Building : MonoBehaviour
{
    private HealthSystem _healthSystem;
    private BuildingTypeSO _buildingType;
    private Transform _buildingDemolishBtn;
    private Transform _buildingRepairBtn;

    private void Start()
    {
        _buildingType = GetComponent<BuildingTypeHolder>().buildingType;

        _healthSystem = GetComponent<HealthSystem>();

        _healthSystem.SetHealthAmountMax(_buildingType.healthAmountMax, true);

        _healthSystem.OnDied += HealthSystem_OnDied;
        _healthSystem.OnDamaged += HealthSystem_OnDamaged;
        _healthSystem.OnHeal += HealthSystem_OnHeal;
    }

    private void HealthSystem_OnHeal(object sender, System.EventArgs e)
    {
        if (_healthSystem.IsFullHealth())
        {
            HideBuildingRepairBtn();
        }
    }

    private void HealthSystem_OnDamaged(object sender, System.EventArgs e)
    {
        ShowBuildingRepairBtn();
    }

    private void Awake()
    {
        _buildingDemolishBtn = transform.Find("pfBuildingDemolishBtn");
        _buildingRepairBtn = transform.Find("pfBuildingRepairBtn");

        HideBuildingDemolishBtn();
        HideBuildingRepairBtn();
    }

    private void HealthSystem_OnDied(object sender, System.EventArgs e)
    {
        Destroy(gameObject);
    }

    private void OnMouseEnter()
    {
        ShowBuildingDemolishBtn();
    }

    private void OnMouseExit()
    {
        HideBuildingDemolishBtn();
    }

    private void ShowBuildingDemolishBtn()
    {
        if (_buildingDemolishBtn != null)
        {
            _buildingDemolishBtn.gameObject.SetActive(true);
        }
    }

    private void HideBuildingDemolishBtn()
    {
        if (_buildingDemolishBtn != null)
        {
            _buildingDemolishBtn.gameObject.SetActive(false);
        }
    }
    
    private void ShowBuildingRepairBtn()
    {
        if (_buildingRepairBtn != null)
        {
            _buildingRepairBtn.gameObject.SetActive(true);
        }
    }

    private void HideBuildingRepairBtn()
    {
        if (_buildingRepairBtn != null)
        {
            _buildingRepairBtn.gameObject.SetActive(false);
        }
    }
}
