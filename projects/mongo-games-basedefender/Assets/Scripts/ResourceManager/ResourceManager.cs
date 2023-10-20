using System;
using System.Collections.Generic;
using UnityEngine;

public class ResourceManager : MonoBehaviour
{
    [SerializeField] private List<ResourceAmount> _startingResourceAmountList;
    public static ResourceManager Instance { get; private set; }
    public event EventHandler OnResourceAmountChanged;

    private Dictionary<ResourceTypeSO, int> _resourceAmountDictionary;

    private void Awake()
    {
        Instance = this;

        _resourceAmountDictionary = new Dictionary<ResourceTypeSO, int>();

        ResourceTypeListSO resourceTypeList = Resources.Load<ResourceTypeListSO>(typeof(ResourceTypeListSO).Name);

        foreach (ResourceTypeSO resourceType in resourceTypeList.list)
        {
            _resourceAmountDictionary[resourceType] = 0;
        }

        foreach (ResourceAmount resourceAmount in _startingResourceAmountList)
        {
            AddResource(resourceAmount.resourceType, resourceAmount.amount);
        }
    }

    public void AddResource(ResourceTypeSO resourceType, int amount)
    {
        _resourceAmountDictionary[resourceType] += amount;

        OnResourceAmountChanged?.Invoke(this, EventArgs.Empty);
    }

    public int GetResourceAmount(ResourceTypeSO resourceType)
    {
        return _resourceAmountDictionary[resourceType];
    }

    public bool CanAfford(ResourceAmount[] resourceAmountArray)
    {
        foreach (ResourceAmount resourceAmount in resourceAmountArray)
        {
            if (GetResourceAmount(resourceAmount.resourceType) >= resourceAmount.amount)
            {

            }
            else
            {
                return false;
            }
        }
        return true;
    }

    public void SpendResources(ResourceAmount[] resourceAmountArray)
    {
        foreach (ResourceAmount resourceAmount in resourceAmountArray)
        {
            _resourceAmountDictionary[resourceAmount.resourceType] -= resourceAmount.amount;
        }
    }
}
