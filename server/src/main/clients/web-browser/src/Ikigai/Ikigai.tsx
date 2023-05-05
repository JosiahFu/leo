import './Ikigai.scss';
import {createRef, PropsWithChildren, useEffect, useState} from 'react';
import {IkigaiCategory} from '../IkigaiCategory/IkigaiCategory';
import {doTransition, overshootTransition} from '../utils/transitions';
import {SpinButton, SpinButtonFunctions} from './SpinButton/SpinButton';

export type Coordinate = {
  x: number;
  y: number;
};

export function Ikigai(
  props: PropsWithChildren<{
    id: string;
    // Setting this to null will hide the diagram.
    centerPosition: Coordinate | null;
    categoryDiameter: number;
    distanceToCategoryCenter: number;
    radians: number;
    radiansOffset?: number;
    enabled: boolean;
    processing?: boolean;
    showSpinButton: boolean;
    onSpinClick: () => void;
    categoryElementIds: (string | undefined)[];
  }>
) {
  const visibleAlpha = 0.2;
  const showHideDurationMs = 750;
  const processingStepDurationMs = 750;
  const processingStepDelayMs = 250;
  const processingStepIncrement = Math.PI / 4;
  const radiansOffset =
    props.radiansOffset != null
      ? props.radiansOffset
      : (2 * Math.PI) / props.categoryElementIds.length / 2;

  const [centerPosition, setCenterPosition] = useState<Coordinate | null>(null);
  const [categoryDiameter, setCategoryDiameter] = useState(0);
  const [distanceToCategoryCenter, setDistanceToCategoryCenter] = useState(0);

  const [radians, setRadians] = useState(props.radians || 0);
  const [alpha, setAlpha] = useState(0);

  const spinButton = createRef<SpinButtonFunctions>();
  const [spinButtonEnabled, setSpinButtonEnabled] = useState(false);

  function show(durationMs: number): Promise<void> {
    const promise = doTransition(
      durationMs,
      {
        setFn: setRadians,
        begin: (props.radians || 0) - 4 * Math.PI,
        end: props.radians || 0,
      },
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
      {setFn: setRadians, begin: radians - 2 * Math.PI, end: 0},
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

  useEffect(() => {
    if (props.showSpinButton) {
      spinButton.current
        ?.show(showHideDurationMs)
        .finally(() => setSpinButtonEnabled(true));
    } else {
      spinButton.current
        ?.hide(showHideDurationMs)
        .finally(() => setSpinButtonEnabled(false));
    }
  }, [props.showSpinButton]);

  function doProcessingStep(startRadians: number) {
    if (props.processing !== true) {
      return;
    }

    doTransition(processingStepDurationMs, {
      setFn: setRadians,
      begin: startRadians - processingStepIncrement,
      end: startRadians,
      fractionFn: overshootTransition(-0.25, 0.7),
    }).finally(() => {
      setTimeout(() => {
        doProcessingStep(startRadians + processingStepIncrement);
      }, processingStepDelayMs);
    });
  }

  useEffect(() => {
    if (props.processing !== false) {
      doProcessingStep(Math.PI / 4 - 0.00001);
    }
  }, [props.processing]);

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
        {props.categoryElementIds.map((categoryElementId, index) => (
          <IkigaiCategory
            id={props.id + '.' + index}
            key={index}
            center={centerPosition || {x: 0, y: 0}}
            diameter={categoryDiameter}
            maxDiameter={props.categoryDiameter}
            hue={index * (360 / props.categoryElementIds.length)}
            alpha={alpha}
            radians={
              radians +
              ((2 * Math.PI) / props.categoryElementIds.length) * index +
              radiansOffset
            }
            distance={distanceToCategoryCenter}
            categoryElementId={categoryElementId}
          />
        ))}
        {props.children}
        <SpinButton
          id={props.id + '.spinButton'}
          origin={centerPosition || {x: 0, y: 0}}
          diameter={categoryDiameter / 3}
          enabled={spinButtonEnabled && props.enabled}
          onClick={props.onSpinClick}
          ref={spinButton}
        />
      </div>
    </>
  );
}
