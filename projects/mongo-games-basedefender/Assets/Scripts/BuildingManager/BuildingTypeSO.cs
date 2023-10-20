using UnityEngine;

[CreateAssetMenu(menuName = "ScriptableObjects/BuildingType")]
public class BuildingTypeSO : ScriptableObject
{
    public string nameString;
    public bool hasResourceGeneratorData;
    public Transform prefab;
    public ResourceGeneratorData resourceGeneratorData;
    public Sprite sprite;
    public float minConstructionRadius;
    public ResourceAmount[] constructionResourceCostArray;
    public int healthAmountMax;
    public float constructionTimerMax;

    public string GetConstructionResourceCostString()
    {
        string str = string.Empty;

        foreach (ResourceAmount resourceAmount in constructionResourceCostArray)
        {
            str += "<color=#" + resourceAmount.resourceType.colorHex + ">"
                + resourceAmount.resourceType.nameShort + ":"
                + resourceAmount.amount
                + "</color> ";
        }
        return str;
    }
}