using UnityEngine;

public class SpritePositionSortingOrder : MonoBehaviour
{
    [SerializeField] private bool _runOnce;
    [SerializeField] private float _positionOffsetY;
    private SpriteRenderer _spriteRenderer;

    private void Awake()
    {
        _spriteRenderer = GetComponent<SpriteRenderer>();
    }

    private void LateUpdate()
    {
        float precisionMultiplier = 5f;
        _spriteRenderer.sortingOrder = (int)(-(transform.position.y + _positionOffsetY) * precisionMultiplier);

        if (_runOnce) Destroy(this);
    }
}
