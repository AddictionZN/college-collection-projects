using UnityEngine;
using UnityEngine.UI;

public class ConstructionTimerUI : MonoBehaviour
{
    [SerializeField] private BuildingConstruction _buildingConstruction;
    
    private Image _constructionProgressImage;

    private void Awake()
    {
        _constructionProgressImage = transform.Find("mask").Find("image").GetComponent<Image>();
    }
    
    private void Update()
    {
        _constructionProgressImage.fillAmount = _buildingConstruction.GetConstructionTimerNomalized();
    }
}
