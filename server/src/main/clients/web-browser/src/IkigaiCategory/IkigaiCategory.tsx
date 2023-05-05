import './IkigaiCategory.scss';
import {PropsWithChildren} from 'react';

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

export function IkigaiCategory(
  props: PropsWithChildren<{
    id: string;
    center: {x: number; y: number};
    diameter: number;
    maxDiameter: number;
    alpha: number;
    radians: number;
    textRadians?: number;
    distance: number;
    categoryElementId: string | undefined;
    hue: number;
  }>
) {
  const x =
    props.center.x +
    Math.cos(props.radians) * props.distance -
    props.diameter / 2;
  const y =
    props.center.y +
    Math.sin(props.radians) * props.distance -
    props.diameter / 2;
  const edgeAt45Deg = (props.diameter / 2) * Math.cos(0.25 * Math.PI);
  const scale =
    props.maxDiameter !== 0 ? props.diameter / props.maxDiameter : 0;

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

    // Position vertically.
    switch (orientation) {
      case Orientation.NORTHEAST:
      case Orientation.NORTH:
      case Orientation.NORTHWEST:
        Object.assign(style, {
          justifyContent: 'initial',
        });
        break;
      case Orientation.SOUTHEAST:
      case Orientation.SOUTH:
      case Orientation.SOUTHWEST:
        Object.assign(style, {
          justifyContent: 'flex-end',
        });
        break;
      case Orientation.EAST:
      case Orientation.WEST:
        Object.assign(style, {
          justifyContent: 'center',
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
          alignItems: 'right',
        });
        break;
      case Orientation.SOUTHWEST:
      case Orientation.WEST:
      case Orientation.NORTHWEST:
        Object.assign(style, {
          textAlign: 'left',
          float: 'left',
          alignItems: 'left',
        });
        break;
      case Orientation.NORTH:
      case Orientation.SOUTH:
        Object.assign(style, {
          textAlign: 'center',
          marginLeft: 'auto',
          marginRight: 'auto',
          float: 'center',
          alignItems: 'center',
        });
        break;
    }
  }

  if (props.categoryElementId != null) {
    const element = document.getElementById(props.categoryElementId);
    if (element != null) {
      Object.assign<CSSStyleDeclaration, Partial<CSSStyleDeclaration>>(
        element.style,
        {
          display: 'flex',
          flexFlow: 'column nowrap',
          position: 'absolute',
          left: x + 'px',
          top: y + 'px',
          width: props.diameter + 'px',
          height: props.diameter + 'px',
          padding: props.diameter / 2 - edgeAt45Deg + 'px',
          borderRadius: '50%',
          transform: `scale(${scale}, ${scale})`,
          visibility: 'visible',
          backgroundColor: `hsla(${props.hue}, 100%, 75%, ${props.alpha})`,
          borderStyle: 'solid',
          borderWidth: '1px',
          borderColor: `hsla(${props.hue}, 100%, 80%, ${props.alpha})`,
        }
      );
      getOrientationStyle(element.style);
    }
  }

  return <></>;
}
