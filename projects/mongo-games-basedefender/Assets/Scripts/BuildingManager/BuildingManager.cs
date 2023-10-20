using System;
using UnityEngine;
using UnityEngine.EventSystems;

public class BuildingManager : MonoBehaviour
{
    [SerializeField] private Building _hqBuilding;

    public static BuildingManager Instance { get; set; }
    private BuildingTypeSO _activeBuildingType;
    private BuildingTypeListSO _buildingTypeList;
    private Camera _mainCamera;

    public event EventHandler<OnActiveBuildingTypeChangedEventArgs> OnActiveBuildingTypeChanged;

    private void Awake()
    {
        Instance = this;
        _buildingTypeList = Resources.Load<BuildingTypeListSO>(typeof(BuildingTypeListSO).Name);
    }

    private void Start()
    {
        _mainCamera = Camera.main;

        _hqBuilding.GetComponent<HealthSystem>().OnDied += HQ_OnDied;
    }

    private void HQ_OnDied(object sender, EventArgs e)
    {
        GameOverUI.Instance.Show();
    }

    private void Update()
    {
        if (Input.GetMouseButtonDown(0) && !EventSystem.current.IsPointerOverGameObject())
        {
            if (_activeBuildingType != null)
            {
                if (CanSpawnBuilding(_activeBuildingType, UtilsClass.GetMouseWorldPosition(), out string errorMessage))
                {
                    if (ResourceManager.Instance.CanAfford(_activeBuildingType.constructionResourceCostArray))
                    {
                        ResourceManager.Instance.SpendResources(_activeBuildingType.constructionResourceCostArray);
                        //Instantiate(_activeBuildingType.prefab, UtilsClass.GetMouseWorldPosition(), Quaternion.identity);
                        BuildingConstruction.Create(UtilsClass.GetMouseWorldPosition(), _activeBuildingType);
                    }
                    else
                    {
                        TooltipUI.Instance.Show("Cannot Afford " + _activeBuildingType.GetConstructionResourceCostString(), new TooltipUI.TooltipTimer { timer = 2f });
                    }
                }
                else
                {
                    TooltipUI.Instance.Show(errorMessage, new TooltipUI.TooltipTimer { timer = 2f });
                }
            }
        }
    }

    public void SetActiveBuildingType(BuildingTypeSO buildingType)
    {
        _activeBuildingType = buildingType;

        OnActiveBuildingTypeChanged?.Invoke(this, new OnActiveBuildingTypeChangedEventArgs
        {
            activeBuildingType = _activeBuildingType
        });
    }

    public BuildingTypeSO GetActiveBuildingType()
    {
        return _activeBuildingType;
    }

    public class OnActiveBuildingTypeChangedEventArgs : EventArgs
    {
        public BuildingTypeSO activeBuildingType;
    }

    private bool CanSpawnBuilding(BuildingTypeSO buildingType, Vector3 position, out string errorMessage)
    {
        BoxCollider2D boxCollider2D = buildingType.prefab.GetComponent<BoxCollider2D>();

        Collider2D[] collider2DArray = Physics2D.OverlapBoxAll(position + (Vector3)boxCollider2D.offset, boxCollider2D.size, 0);

        bool isAreaClear = collider2DArray.Length == 0;

        if (!isAreaClear)
        {
            errorMessage = "Area is not clear!";
            return false;
        }

        collider2DArray = Physics2D.OverlapCircleAll(position, buildingType.minConstructionRadius);

        foreach (Collider2D collider2D in collider2DArray)
        {
            // Colliders inside the construction of the radius       
            BuildingTypeHolder buildingTypeHolder = collider2D.GetComponent<BuildingTypeHolder>();

            if (buildingTypeHolder != null)
            {
                // Contains a BuildingTypeHolder
                if (buildingTypeHolder.buildingType == buildingType)
                {
                    errorMessage = "Too Close to another building of the same type!";
                    // Theres already a building of this type within the construction of this radius
                    return false;
                }
            }
        }

        float maxConstructionRadius = 25f;
        collider2DArray = Physics2D.OverlapCircleAll(position, maxConstructionRadius);

        foreach (Collider2D collider2D in collider2DArray)
        {
            // Colliders inside the construction of the radius       
            BuildingTypeHolder buildingTypeHolder = collider2D.GetComponent<BuildingTypeHolder>();

            if (buildingTypeHolder != null)
            {
                errorMessage = string.Empty;
                // It is a building
                return true;
            }
        }
        errorMessage = "Too far from any other building!";
        return false;
    }

    public Building GetHQBuilding()
    {
        return _hqBuilding;
    }
}
