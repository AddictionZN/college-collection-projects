using UnityEngine;
using UnityEngine.EventSystems;
using System;

public class MouseEnterExitEvents : MonoBehaviour, IPointerEnterHandler, IPointerExitHandler
{
    public event EventHandler OnMouseEnter;
    public event EventHandler OnMouseExit;

    public void OnPointerEnter(PointerEventData eventData)
    {
        OnMouseEnter?.Invoke(this, EventArgs.Empty);
    }

    public void OnPointerExit(PointerEventData eventData)
    {
        OnMouseExit?.Invoke(this, EventArgs.Empty);
    }
}
