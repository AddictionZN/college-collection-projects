using TMPro;
using UnityEngine;

public class ResourceNearbyOverlay : MonoBehaviour
{
    private ResourceGeneratorData _resourceGeneratorData;

    private void Awake()
    {
        Hide();
    }

    private void Update()
    {
        int nearbyResourceAmount = ResourceGenerator.GetNearbyResourceAmount(_resourceGeneratorData, transform.position);
        float percent = Mathf.RoundToInt((float)nearbyResourceAmount / _resourceGeneratorData.maxResourceAmount * 100f);

        transform.Find("text").GetComponent<TextMeshPro>().SetText(percent + "%");
    }

    public void Show(ResourceGeneratorData resourceGeneratorData)
    {
        _resourceGeneratorData = resourceGeneratorData;
        gameObject.SetActive(true);

        transform.Find("icon").GetComponent<SpriteRenderer>().sprite = _resourceGeneratorData.resourceType.sprite;
    }

    public void Hide()
    {
        gameObject.SetActive(false);
    }
}
