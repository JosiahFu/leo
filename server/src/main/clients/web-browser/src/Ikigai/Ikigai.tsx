import './Ikigai.scss';
import {createRef, ReactNode, useEffect, useState} from 'react';
import {IkigaiCategory} from '../IkigaiCategory/IkigaiCategory';
import {doTransition, overshootTransition} from '../utils/transitions';
import {SpinButton, SpinButtonFunctions} from './SpinButton/SpinButton';

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
  radians?: number;
  enabled?: boolean;
  processing?: boolean;

  lovesResizeAndRotateElement: ReactNode;
  lovesValueIsSet?: number;
  onLovesClick: () => void;

  worldNeedsResizeAndRotateElement: ReactNode;
  worldNeedsValueIsSet?: number;
  onWorldNeedsClick: () => void;

  paidForResizeAndRotateElement: ReactNode;
  paidForValueIsSet?: number;
  onPaidForClick: () => void;

  goodAtResizeAndRotateElement: ReactNode;
  goodAtValueIsSet?: number;
  onGoodAtClick: () => void;

  showSpinButton: boolean;
  onSpinClick: () => void;
}) {
  const visibleAlpha = 0.2;
  const showHideDurationMs = 750;
  const processingStepDurationMs = 400;
  const processingStepDelayMs = 125;
  const processingStepIncrement = Math.PI / 4;

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
      {setFn: setRadians, begin: radians - 4 * Math.PI, end: 0},
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
        <div id={props.id + '.lovesPanel'} onClick={props.onLovesClick}>
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
          onClick={props.enabled !== false ? props.onLovesClick : () => {}}
          highlightBackground={
            props.lovesValueIsSet != null ? props.lovesValueIsSet : 1
          }
        />
        <div
          id={props.id + '.worldNeedsPanel'}
          onClick={props.onWorldNeedsClick}
        >
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
          onClick={props.enabled !== false ? props.onWorldNeedsClick : () => {}}
          highlightBackground={
            props.worldNeedsValueIsSet != null ? props.worldNeedsValueIsSet : 1
          }
        />
        <div id={props.id + '.paidForPanel'} onClick={props.onPaidForClick}>
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
          onClick={props.enabled !== false ? props.onPaidForClick : () => {}}
          highlightBackground={
            props.paidForValueIsSet != null ? props.paidForValueIsSet : 1
          }
        />
        <div id={props.id + '.goodAtPanel'} onClick={props.onGoodAtClick}>
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
          onClick={props.enabled !== false ? props.onGoodAtClick : () => {}}
          highlightBackground={
            props.goodAtValueIsSet != null ? props.goodAtValueIsSet : 1
          }
        />
        <SpinButton
          id={props.id + '.spinButton'}
          origin={centerPosition || {x: 0, y: 0}}
          diameter={categoryDiameter / 3}
          enabled={props.enabled !== false && spinButtonEnabled}
          onClick={props.onSpinClick}
          ref={spinButton}
        />
      </div>
    </>
  );
}
