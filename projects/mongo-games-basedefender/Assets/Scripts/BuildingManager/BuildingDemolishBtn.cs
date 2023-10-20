using UnityEngine;
using UnityEngine.UI;

public class BuildingDemolishBtn : MonoBehaviour
{
    [SerializeField] private Building _building;

    private void Awake()
    {
        transform.Find("button").GetComponent<Button>().onClick.AddListener(() =>
        {
            BuildingTypeSO buildingType = _building.GetComponent<BuildingTypeHolder>().buildingType;

            foreach (ResourceAmount resourceAmount in buildingType.constructionResourceCostArray)
            {
                ResourceManager.Instance.AddResource(resourceAmount.resourceType, Mathf.FloorToInt(resourceAmount.amount * .6f));
            }

            Destroy(_building.gameObject);
        });
    }
}
