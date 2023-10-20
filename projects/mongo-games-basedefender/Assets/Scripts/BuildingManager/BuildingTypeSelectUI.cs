using System;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class BuildingTypeSelectUI : MonoBehaviour
{
    [SerializeField] private Sprite _arrowSprite;
    [SerializeField] private List<BuildingTypeSO> _ignoreBuildingTypeList;

    private Dictionary<BuildingTypeSO, Transform> _btnTransformDictionary;
    private Transform _btnArrow;

    private void Awake()
    {
        Transform btnTemplate = transform.Find("btnTemplate");
        btnTemplate.gameObject.SetActive(false);

        BuildingTypeListSO buildingTypeList = Resources.Load<BuildingTypeListSO>(typeof(BuildingTypeListSO).Name);

        _btnTransformDictionary = new Dictionary<BuildingTypeSO, Transform>();

        int index = 0;

        _btnArrow = Instantiate(btnTemplate, transform);
        _btnArrow.gameObject.SetActive(true);

        _btnArrow.Find("image").GetComponent<Image>().sprite = _arrowSprite;
        _btnArrow.Find("image").GetComponent<RectTransform>().sizeDelta = new Vector2(0, -50);

        float offsetAmount = +130f;
        _btnArrow.GetComponent<RectTransform>().anchoredPosition = new Vector2(offsetAmount * index, 0);

        _btnArrow.GetComponent<Button>().onClick.AddListener(() =>
        {
            BuildingManager.Instance.SetActiveBuildingType(null);
        });

        MouseEnterExitEvents mouseEnterExitEvents = _btnArrow.GetComponent<MouseEnterExitEvents>();
        mouseEnterExitEvents.OnMouseEnter += (object sender, EventArgs e) =>
        {
            TooltipUI.Instance.Show("Arrow");
        };
        mouseEnterExitEvents.OnMouseExit += (object sender, EventArgs e) =>
        {
            TooltipUI.Instance.Hide();
        };

        index++;

        foreach (BuildingTypeSO buildingType in buildingTypeList.list)
        {
            if (_ignoreBuildingTypeList.Contains(buildingType)) continue;

            Transform btnTransform = Instantiate(btnTemplate, transform);

            btnTransform.gameObject.SetActive(true);

            offsetAmount = +130f;

            btnTransform.GetComponent<RectTransform>().anchoredPosition = new Vector2(offsetAmount * index, 0);

            btnTransform.Find("image").GetComponent<Image>().sprite = buildingType.sprite;

            btnTransform.GetComponent<Button>().onClick.AddListener(() =>
            {
                BuildingManager.Instance.SetActiveBuildingType(buildingType);
            });

            mouseEnterExitEvents = btnTransform.GetComponent<MouseEnterExitEvents>();
            mouseEnterExitEvents.OnMouseEnter += (object sender, EventArgs e) =>
            {
                TooltipUI.Instance.Show(buildingType.nameString + "\n" + buildingType.GetConstructionResourceCostString());
            };
            mouseEnterExitEvents.OnMouseExit += (object sender, EventArgs e) =>
            {
                TooltipUI.Instance.Hide();
            };

            _btnTransformDictionary[buildingType] = btnTransform;

            index++;
        }
    }

    private void Start()
    {
        BuildingManager.Instance.OnActiveBuildingTypeChanged += BuildingManager_OnActiveBuildingTypeChanged;
        UpdatedActiveBuildingTypeButton();
    }

    private void BuildingManager_OnActiveBuildingTypeChanged(object sender, BuildingManager.OnActiveBuildingTypeChangedEventArgs e)
    {
        UpdatedActiveBuildingTypeButton();
    }

    private void UpdatedActiveBuildingTypeButton()
    {
        _btnArrow.Find("selected").gameObject.SetActive(false);

        foreach (BuildingTypeSO buildingType in _btnTransformDictionary.Keys)
        {
            Transform btnTransform = _btnTransformDictionary[buildingType];
            btnTransform.Find("selected").gameObject.SetActive(false);
        }

        BuildingTypeSO activeBuildingType = BuildingManager.Instance.GetActiveBuildingType();

        if (activeBuildingType == null)
        {
            _btnArrow.Find("selected").gameObject.SetActive(true);
        }
        else
        {
            _btnTransformDictionary[activeBuildingType].Find("selected").gameObject.SetActive(true);
        }
    }
}
