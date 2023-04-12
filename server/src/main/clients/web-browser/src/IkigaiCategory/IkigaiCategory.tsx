import './IkigaiCategory.scss';

enum Orientation {
  TOP,
  LEFT,
  BOTTOM,
  RIGHT,
}

export function IkigaiCategory(props: {
  id: string;
  center: {x: number; y: number};
  diameter: number;
  color: {r: number; g: number; b: number};
  alpha: number;
  radians: number;
  textRadians?: number;
  distance: number;
  resizeAndRotateElementIds: string[];
  onClick: () => void;
  // From 0 (gray with only colored border) to 1 (colored with border).
  highlightBackground: number;
}) {
  const x =
    props.center.x +
    Math.cos(props.radians) * props.distance -
    props.diameter / 2;
  const y =
    props.center.y +
    Math.sin(props.radians) * props.distance -
    props.diameter / 2;
  const edgeAt45Deg = (props.diameter / 2) * Math.cos(0.25 * Math.PI);
  const grayRgb = 192;

  function getOrientationStyle(style: CSSStyleDeclaration) {
    const normalizedRadians =
      ((props.radians % (2 * Math.PI)) + 2 * Math.PI) % (2 * Math.PI);

    const orientation =
      normalizedRadians < Math.PI / 4
        ? Orientation.RIGHT
        : normalizedRadians < (3 * Math.PI) / 4
        ? Orientation.BOTTOM
        : normalizedRadians < Math.PI + Math.PI / 4
        ? Orientation.LEFT
        : normalizedRadians < Math.PI + (3 * Math.PI) / 4
        ? Orientation.TOP
        : Orientation.RIGHT;

    switch (orientation) {
      case Orientation.LEFT:
        Object.assign(style, {
          display: 'flex',
          textAlign: 'left',
          float: 'left',
          alignItems: 'center',
        });
        break;
      case Orientation.RIGHT:
        Object.assign(style, {
          display: 'flex',
          textAlign: 'right',
          float: 'right',
          alignItems: 'center',
        });
        break;
      case Orientation.TOP:
        Object.assign(style, {
          display: 'flex',
          textAlign: 'center',
          float: 'initial',
          marginLeft: 'auto',
          marginRight: 'auto',
          alignItems: 'initial',
        });
        break;
      case Orientation.BOTTOM:
        Object.assign(style, {
          display: 'flex',
          textAlign: 'center',
          float: 'flex-end',
          marginLeft: 'auto',
          marginRight: 'auto',
          alignItems: 'flex-end',
        });
        break;
    }
  }

  for (let i = 0; i < props.resizeAndRotateElementIds.length; ++i) {
    const resizeAndRotateElement = document.getElementById(
      props.resizeAndRotateElementIds[i]
    );
    if (resizeAndRotateElement) {
      resizeAndRotateElement.style.position = 'absolute';
      resizeAndRotateElement.style.left =
        (x + (props.diameter / 2 - edgeAt45Deg)).toString() + 'px';
      resizeAndRotateElement.style.top =
        (y + (props.diameter / 2 - edgeAt45Deg)).toString() + 'px';
      resizeAndRotateElement.style.width = (2 * edgeAt45Deg).toString() + 'px';
      resizeAndRotateElement.style.height = (2 * edgeAt45Deg).toString() + 'px';
      resizeAndRotateElement.style.fontSize =
        (props.diameter / 12).toString() + 'px';
      // Initially, these are set to hidden so that they don't appear before they are positioned.
      resizeAndRotateElement.style.visibility = 'visible';
      getOrientationStyle(resizeAndRotateElement.style);
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
          width: props.diameter,
          top: y,
          height: props.diameter,
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
          border: `2px solid rgb(
          ${props.color.r},
          ${props.color.g},
          ${props.color.b},
          ${props.alpha * 2})`,
        }}
        onClick={props.onClick}
      />
    </>
  );
}
