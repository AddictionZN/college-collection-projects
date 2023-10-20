using UnityEngine;
using UnityEngine.UI;

public class BuildingRepairBtn : MonoBehaviour
{
    [SerializeField] private HealthSystem _healthSystem;
    [SerializeField] private ResourceTypeSO _goldResourceType;

    private void Awake()
    {
        transform.Find("button").GetComponent<Button>().onClick.AddListener(() =>
        {
            int missingHealth = _healthSystem.GetHealthAmountMax() - _healthSystem.GetHealthAmount();
            int repairCost = missingHealth / 2;

            ResourceAmount[] resourceAmountCost = new ResourceAmount[]
            {
                new ResourceAmount
                {
                    resourceType = _goldResourceType,
                    amount = repairCost
                }
            };

            if (ResourceManager.Instance.CanAfford(resourceAmountCost))
            {
                ResourceManager.Instance.SpendResources(resourceAmountCost);
                _healthSystem.HealFull();
            }
            else
            {
                TooltipUI.Instance.Show("Cannot afford repair cost!", new TooltipUI.TooltipTimer { timer = 2f });
            }
        });
    }
}
