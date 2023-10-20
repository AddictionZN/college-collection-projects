using Cinemachine;
using UnityEngine;

public class CameraHandler : MonoBehaviour
{
    [SerializeField] private CinemachineVirtualCamera _cinemachineVirtualCamera;
    private float _orthographicSize;
    private float _targetOrthographicSize;

    private void Start()
    {
        _orthographicSize = _cinemachineVirtualCamera.m_Lens.OrthographicSize;
        _targetOrthographicSize = _orthographicSize;
    }

    private void Update()
    {
        HandleMovement();
        HandleZoom();
    }

    private void HandleMovement()
    {
        float x = Input.GetAxisRaw("Horizontal");
        float y = Input.GetAxisRaw("Vertical");

        Vector3 moveDirection = new Vector3(x, y).normalized;
        float moveSpeed = 30f;

        transform.position += moveDirection * moveSpeed * Time.deltaTime;
    }

    private void HandleZoom()
    {
        float zoomAmount = 2f;

        _targetOrthographicSize += -Input.mouseScrollDelta.y * zoomAmount;

        float minOrthographicSize = 10f;
        float maxOrthographicSize = 30f;

        _targetOrthographicSize = Mathf.Clamp(_targetOrthographicSize, minOrthographicSize, maxOrthographicSize);


        float zoomSpeed = 5f;
        _orthographicSize = Mathf.Lerp(_orthographicSize, _targetOrthographicSize, Time.deltaTime * zoomSpeed);

        _cinemachineVirtualCamera.m_Lens.OrthographicSize = _orthographicSize;
    }
}
