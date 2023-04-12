import './Ikigai.scss';
import {ReactNode, useEffect, useState} from 'react';
import {IkigaiCategory} from '../IkigaiCategory/IkigaiCategory';
import {doTransition} from '../utils/transitions';

export type Coordinate = {
  x: number;
  y: number;
};

export function Ikigai(props: {
  id: string;
  // Setting this to null will hide the diagram.
  centerPosition: Coordinate | null;
  categoryDiameter: number;
  distanceToCategoryCenter: number;

  lovesResizeAndRotateElement: ReactNode;
  lovesValueIsSet?: number;

  worldNeedsResizeAndRotateElement: ReactNode;
  worldNeedsValueIsSet?: number;

  paidForResizeAndRotateElement: ReactNode;
  paidForValueIsSet?: number;

  goodAtResizeAndRotateElement: ReactNode;
  goodAtValueIsSet?: number;
}) {
  const visibleAlpha = 0.2;
  const showHideDurationMs = 750;

  const [centerPosition, setCenterPosition] = useState<Coordinate | null>(null);
  const [categoryDiameter, setCategoryDiameter] = useState(0);
  const [distanceToCategoryCenter, setDistanceToCategoryCenter] = useState(0);

  const [radians, setRadians] = useState(0);
  const [alpha, setAlpha] = useState(0);

  function show(durationMs: number): Promise<void> {
    const promise = doTransition(
      durationMs,
      {setFn: setRadians, begin: radians + 4 * Math.PI, end: 0},
      {
        setFn: setDistanceToCategoryCenter,
        begin: 0,
        end: props.distanceToCategoryCenter,
      },
      {
        setFn: setAlpha,
        begin: 0,
        end: visibleAlpha,
      },
      {
        setFn: setCategoryDiameter,
        begin: 0,
        end: props.categoryDiameter,
      }
    ) as Promise<unknown>;

    return promise as Promise<void>;
  }

  function hide(durationMs: number): Promise<void> {
    const promise = doTransition(
      durationMs,
      {setFn: setRadians, begin: radians + 4 * Math.PI, end: 0},
      {
        setFn: setDistanceToCategoryCenter,
        begin: props.distanceToCategoryCenter,
        end: 0,
      },
      {setFn: setAlpha, begin: visibleAlpha, end: 0},
      {setFn: setCategoryDiameter, begin: props.categoryDiameter, end: 0}
    ) as Promise<unknown>;

    return promise as Promise<void>;
  }

  // Reposition and show the diagram when its properties are set/changed.
  useEffect(() => {
    if (props.centerPosition == null) {
      if (centerPosition != null) {
        hide(showHideDurationMs).finally(() => setCenterPosition(null));
      }
    } else if (centerPosition == null) {
      setCenterPosition(props.centerPosition);
      show(showHideDurationMs);
    } else {
      setCenterPosition(props.centerPosition);
      setCategoryDiameter(props.categoryDiameter);
      setDistanceToCategoryCenter(props.distanceToCategoryCenter);
    }
  }, [
    props.centerPosition,
    props.categoryDiameter,
    props.distanceToCategoryCenter,
  ]);

  return (
    <>
      <div style={{visibility: centerPosition ? 'visible' : 'hidden'}}>
        <div id={props.id + '.lovesPanel'}>
          <div>{props.lovesResizeAndRotateElement}</div>
        </div>
        <IkigaiCategory
          id={props.id + '.lovesCategory'}
          center={centerPosition || {x: 0, y: 0}}
          diameter={categoryDiameter}
          color={{r: 249, g: 209, b: 98}}
          alpha={alpha}
          radians={radians + 1.5 * Math.PI}
          distance={distanceToCategoryCenter}
          resizeAndRotateElementIds={[props.id + '.lovesPanel']}
          highlightBackground={
            props.lovesValueIsSet != null ? props.lovesValueIsSet : 1
          }
        />
        <div id={props.id + '.worldNeedsPanel'}>
          <div>{props.worldNeedsResizeAndRotateElement}</div>
        </div>
        <IkigaiCategory
          id={props.id + '.worldNeedsCategory'}
          center={centerPosition || {x: 0, y: 0}}
          diameter={categoryDiameter}
          color={{r: 243, g: 149, b: 79}}
          alpha={alpha}
          radians={radians + 0 * Math.PI}
          distance={distanceToCategoryCenter}
          resizeAndRotateElementIds={[props.id + '.worldNeedsPanel']}
          highlightBackground={
            props.worldNeedsValueIsSet != null ? props.worldNeedsValueIsSet : 1
          }
        />
        <div id={props.id + '.paidForPanel'}>
          <div>{props.paidForResizeAndRotateElement}</div>
        </div>
        <IkigaiCategory
          id={props.id + '.paidForCategory'}
          center={centerPosition || {x: 0, y: 0}}
          diameter={categoryDiameter}
          color={{r: 107, g: 198, b: 165}}
          alpha={alpha}
          radians={radians + 0.5 * Math.PI}
          distance={distanceToCategoryCenter}
          resizeAndRotateElementIds={[props.id + '.paidForPanel']}
          highlightBackground={
            props.paidForValueIsSet != null ? props.paidForValueIsSet : 1
          }
        />
        <div id={props.id + '.goodAtPanel'}>
          <div>{props.goodAtResizeAndRotateElement}</div>
        </div>
        <IkigaiCategory
          id={props.id + '.goodAtCategory'}
          center={centerPosition || {x: 0, y: 0}}
          diameter={categoryDiameter}
          color={{r: 10, g: 86, b: 136}}
          alpha={alpha}
          radians={radians + Math.PI}
          distance={distanceToCategoryCenter}
          resizeAndRotateElementIds={[props.id + '.goodAtPanel']}
          highlightBackground={
            props.goodAtValueIsSet != null ? props.goodAtValueIsSet : 1
          }
        />
      </div>
    </>
  );
}
