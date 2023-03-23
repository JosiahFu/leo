import './IkigaiCategory.scss';
import {useState} from 'react';

export function IkigaiCategory(props: {
  id: string;
  origin: {x: number; y: number};
  size: number;
  color: {r: number; g: number; b: number};
  alpha: number;
  radians: number;
  textRadians?: number;
  distance: number;
  resizeAndRotateElementIds: string[];
  // From 0 (gray with only colored border) to 1 (colored background no border).
  highlightBackground: number;
}) {
  const x =
    props.origin.x + Math.cos(props.radians) * props.distance - props.size / 2;
  const y =
    props.origin.y + Math.sin(props.radians) * props.distance - props.size / 2;
  const edgeAt45Deg = (props.size / 2) * Math.cos(0.25 * Math.PI);
  const grayRgb = 192;

  const [initialRadians] = useState(props.radians);
  const [initialTextRadians] = useState(props.textRadians || props.radians);

  for (let i = 0; i < props.resizeAndRotateElementIds.length; ++i) {
    const resizeAndRotateElement = document.getElementById(
      props.resizeAndRotateElementIds[i]
    );
    if (resizeAndRotateElement) {
      resizeAndRotateElement.style.rotate =
        (props.radians - initialTextRadians).toString() + 'rad';
      resizeAndRotateElement.style.position = 'absolute';
      resizeAndRotateElement.style.left =
        (x + (props.size / 2 - edgeAt45Deg)).toString() + 'px';
      resizeAndRotateElement.style.top =
        (y + (props.size / 2 - edgeAt45Deg)).toString() + 'px';
      resizeAndRotateElement.style.width = (2 * edgeAt45Deg).toString() + 'px';
      resizeAndRotateElement.style.height = (2 * edgeAt45Deg).toString() + 'px';
      resizeAndRotateElement.style.fontSize =
        (props.size / 12).toString() + 'px';
      // Initially, these are set to hidden so that they don't appear before they are positioned.
      resizeAndRotateElement.style.visibility = 'visible';
    }
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
          ${
            props.highlightBackground * props.color.r +
            (1 - props.highlightBackground) * grayRgb
          },
          ${
            props.highlightBackground * props.color.g +
            (1 - props.highlightBackground) * grayRgb
          },
          ${
            props.highlightBackground * props.color.b +
            (1 - props.highlightBackground) * grayRgb
          },
          ${props.alpha})`,
          border: `2px solid rgba(
          ${props.color.r},
          ${props.color.g},
          ${props.color.b},
          ${(1 - props.highlightBackground) * props.alpha})`,
        }}
      />
    </>
  );
}
