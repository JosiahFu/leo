import './IkigaiCategory.scss';
import {useState} from 'react';

export function IkigaiCategory(props: {
  id: string;
  origin: {x: number; y: number};
  size: number;
  color: {r: number; g: number; b: number};
  alpha: number;
  radians: number;
  distance: number;
  divElementId: string;
  formElementId: string;
  hideFormElement: boolean;
}) {
  const x =
    props.origin.x + Math.cos(props.radians) * props.distance - props.size / 2;
  const y =
    props.origin.y + Math.sin(props.radians) * props.distance - props.size / 2;
  const edgeAt45Deg = (props.size / 2) * Math.cos(0.25 * Math.PI);
  const grayRgb = 192;

  const [initialRadians] = useState(props.radians);
  const [selected, setSelected] = useState(false);

  const formElement = document.getElementById(props.formElementId);
  if (formElement) {
    formElement.onchange = () => {
      setSelected(true);
    };
    if (props.hideFormElement) {
      formElement.style.visibility = 'hidden';
    } else {
      formElement.style.removeProperty('visibility');
    }
  }

  const divElement = document.getElementById(props.divElementId);
  if (divElement) {
    divElement.style.rotate =
      (props.radians - initialRadians).toString() + 'rad';
    divElement.style.position = 'absolute';
    divElement.style.left =
      (x + (props.size / 2 - edgeAt45Deg)).toString() + 'px';
    divElement.style.top =
      (y + (props.size / 2 - edgeAt45Deg)).toString() + 'px';
    divElement.style.width = (2 * edgeAt45Deg).toString() + 'px';
    divElement.style.height = (2 * edgeAt45Deg).toString() + 'px';
    divElement.style.fontSize = (props.size / 12).toString() + 'px';
  }

  return (
    <>
      <div
        id={props.id}
        key={props.id}
        className="ikigai-category"
        style={{
          left: x,
          width: props.size,
          top: y,
          height: props.size,
          backgroundColor: `rgba(
          ${selected ? props.color.r : grayRgb},
          ${selected ? props.color.g : grayRgb},
          ${selected ? props.color.b : grayRgb},
          ${props.alpha})`,
          border: `2px solid rgba(
          ${props.color.r},
          ${props.color.g},
          ${props.color.b},
          ${props.alpha})`,
        }}
      />
    </>
  );
}
