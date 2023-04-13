import './IkigaiCategory.scss';

enum Orientation {
  NORTH,
  NORTHWEST,
  WEST,
  SOUTHWEST,
  SOUTH,
  SOUTHEAST,
  EAST,
  NORTHEAST,
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
    // Force the angle to be between 0 and 2 * Math.PI.
    const normalizedRadians =
      ((props.radians % (2 * Math.PI)) + 2 * Math.PI) % (2 * Math.PI);

    // Split the angle into 16 position, 4 per quadrant.
    const segment = (normalizedRadians * 16) / (2 * Math.PI);

    // Identify the origin for each segment.
    const orientation =
      segment < 1
        ? Orientation.EAST
        : segment < 3
        ? Orientation.SOUTHEAST
        : segment < 5
        ? Orientation.SOUTH
        : segment < 7
        ? Orientation.SOUTHWEST
        : segment < 9
        ? Orientation.WEST
        : segment < 11
        ? Orientation.NORTHWEST
        : segment < 13
        ? Orientation.NORTH
        : segment < 15
        ? Orientation.NORTHEAST
        : Orientation.EAST;

    Object.assign(style, {
      display: 'flex',
    });

    // Position vertically.
    switch (orientation) {
      case Orientation.NORTHEAST:
      case Orientation.NORTH:
      case Orientation.NORTHWEST:
        Object.assign(style, {
          alignItems: 'initial',
        });
        break;
      case Orientation.SOUTHEAST:
      case Orientation.SOUTH:
      case Orientation.SOUTHWEST:
        Object.assign(style, {
          alignItems: 'flex-end',
        });
        break;
      case Orientation.EAST:
      case Orientation.WEST:
        Object.assign(style, {
          alignItems: 'center',
        });
        break;
    }

    // Position horizontally.
    switch (orientation) {
      case Orientation.NORTHEAST:
      case Orientation.EAST:
      case Orientation.SOUTHEAST:
        Object.assign(style, {
          textAlign: 'right',
          float: 'right',
        });
        break;
      case Orientation.SOUTHWEST:
      case Orientation.WEST:
      case Orientation.NORTHWEST:
        Object.assign(style, {
          textAlign: 'left',
          float: 'left',
        });
        break;
      case Orientation.NORTH:
      case Orientation.SOUTH:
        Object.assign(style, {
          textAlign: 'center',
          marginLeft: 'auto',
          marginRight: 'auto',
          float: 'center',
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
