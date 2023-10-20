using UnityEngine;

public class BuildingGhost : MonoBehaviour
{
    private GameObject _spriteGameObject;
    private ResourceNearbyOverlay _resourceNearbyOverlay;

    private void Awake()
    {
        _spriteGameObject = transform.Find("sprite").gameObject;
        _resourceNearbyOverlay = transform.Find("pfResourceNearbyOverlay").GetComponent<ResourceNearbyOverlay>();
        Hide();
    }

    private void Start()
    {
        BuildingManager.Instance.OnActiveBuildingTypeChanged += BuildingManager_OnActiveBuildingTypeChanged;
    }

    private void BuildingManager_OnActiveBuildingTypeChanged(object sender, BuildingManager.OnActiveBuildingTypeChangedEventArgs eventArgs)
    {
        if (eventArgs.activeBuildingType == null)
        {
            Hide();
            _resourceNearbyOverlay.Hide();
        }
        else
        {
            Show(eventArgs.activeBuildingType.sprite);

            if (eventArgs.activeBuildingType.hasResourceGeneratorData)
            {
                _resourceNearbyOverlay.Show(eventArgs.activeBuildingType.resourceGeneratorData);
            }
            else
            {
                _resourceNearbyOverlay.Hide();
            }
        }
    }

    private void Update()
    {
        transform.position = UtilsClass.GetMouseWorldPosition();
    }

    private void Show(Sprite ghostSprite)
    {
        _spriteGameObject.SetActive(true);
        _spriteGameObject.GetComponent<SpriteRenderer>().sprite = ghostSprite;
    }

    private void Hide()
    {
        _spriteGameObject.SetActive(false);
    }
}
