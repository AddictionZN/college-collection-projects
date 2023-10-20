using UnityEngine;

public class BuildingConstruction : MonoBehaviour
{
    private BuildingTypeSO _buildingType;
    private float _constructionTimer;
    private float _constructionTimerMax;
    private BoxCollider2D _boxCollider2D;
    private SpriteRenderer _spriteRenderer;
    private BuildingTypeHolder _buildingTypeHolder;
    private Material _constructionMaterial;

    public static BuildingConstruction Create(Vector3 position, BuildingTypeSO buildingType)
    {
        Transform pfBuildingConstruction = Resources.Load<Transform>("pfBuildingConstruction");
        Transform buildingConstructionTransform = Instantiate(pfBuildingConstruction, position, Quaternion.identity);

        BuildingConstruction buildingConstruction = buildingConstructionTransform.GetComponent<BuildingConstruction>();

        buildingConstruction.SetBuildingType(buildingType);

        return buildingConstruction;
    }

    public float GetConstructionTimerNomalized()
    {
        return 1 - _constructionTimer / _constructionTimerMax;
    }

    private void Awake()
    {
        _boxCollider2D = GetComponent<BoxCollider2D>();
        _spriteRenderer = transform.Find("sprite").GetComponent<SpriteRenderer>();
        _buildingTypeHolder = GetComponent<BuildingTypeHolder>();
        _constructionMaterial = _spriteRenderer.material;
    }

    private void Update()
    {
        _constructionTimer -= Time.deltaTime;

        _constructionMaterial.SetFloat("_Progress", GetConstructionTimerNomalized());
        if (_constructionTimer <= 0f)
        {
            Instantiate(_buildingType.prefab, transform.position, Quaternion.identity);
            Destroy(gameObject);
        }
    }

    private void SetBuildingType(BuildingTypeSO buildingType)
    {
        _constructionTimerMax = buildingType.constructionTimerMax;
        _buildingType = buildingType;
        _constructionTimer = _constructionTimerMax;

        _spriteRenderer.sprite = buildingType.sprite;

        _boxCollider2D.offset = buildingType.prefab.GetComponent<BoxCollider2D>().offset;
        _boxCollider2D.size = buildingType.prefab.GetComponent<BoxCollider2D>().size;

        _buildingTypeHolder.buildingType = buildingType;
    }
}
