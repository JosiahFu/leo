import './Ikigai.scss';
import {forwardRef, Ref, useImperativeHandle, useState} from 'react';
import {IkigaiCategory} from '../IkigaiCategory/IkigaiCategory';
import {doTransition, overshootTransition} from '../utils/transitions';

export type IkigaiFunctions = {
  show: (durationMs: number) => Promise<void>;
  hide: (durationMs: number) => Promise<void>;
};

export const Ikigai = forwardRef(
  (
    props: {
      id: string;
      origin: {x: number; y: number};
      size: number;
      distance: number;
      loveDivElementId: string;
      needsDivElementId: string;
      paidDivElementId: string;
      goodDivElementId: string;
      loveFormElementId: string;
      needsFormElementId: string;
      paidFormElementId: string;
      goodFormElementId: string;
    },
    ref: Ref<IkigaiFunctions>
  ) => {
    const [animating, setAnimating] = useState(true);
    const [radians, setRadians] = useState(0);
    const [distance, setDistance] = useState(0);
    const [alpha, setAlpha] = useState(0);
    const [size, setSize] = useState(0);

    console.log('Distance: ' + distance + ', Prop Distance: ' + props.distance);

    const functions = {
      show(durationMs: number): Promise<void> {
        setAnimating(true);
        return doTransition(
          durationMs,
          {setFn: setRadians, start: radians + 2 * Math.PI, end: radians},
          {
            setFn: setDistance,
            start: distance,
            end: props.distance,
            // fractionFn: overshootTransition(1, 0.9),
          },
          {
            setFn: setAlpha,
            start: alpha,
            end: 0.2,
            // fractionFn: lateTransition(0.8),
          },
          {
            setFn: setSize,
            start: size,
            end: props.size,
            fractionFn: overshootTransition(1.1, 0.9),
          }
        ).then(() => setAnimating(false));
      },

      hide(durationMs: number): Promise<void> {
        setAnimating(true);
        return doTransition(
          durationMs,
          {setFn: setRadians, start: radians + 4 * Math.PI, end: radians},
          {setFn: setDistance, start: props.distance, end: 0},
          {setFn: setAlpha, start: alpha, end: 0},
          {setFn: setSize, start: props.size, end: 0}
        ).then(() => {
          location.reload();
        });
      },
    };
    useImperativeHandle(ref, () => functions);

    return (
      <>
        <IkigaiCategory
          id={props.id + '_loveCategory'}
          origin={props.origin}
          size={animating ? size : props.size}
          color={{r: 249, g: 209, b: 98}}
          alpha={alpha}
          radians={radians + 1.5 * Math.PI}
          distance={animating ? distance : props.distance}
          divElementId={props.loveDivElementId}
          formElementId={props.loveFormElementId}
          hideFormElement={animating}
        />
        <IkigaiCategory
          id={props.id + '_needsCategory'}
          origin={props.origin}
          size={animating ? size : props.size}
          color={{r: 243, g: 149, b: 79}}
          alpha={alpha}
          radians={radians + 0 * Math.PI}
          distance={animating ? distance : props.distance}
          divElementId={props.needsDivElementId}
          formElementId={props.needsFormElementId}
          hideFormElement={animating}
        />
        <IkigaiCategory
          id={props.id + '_paidCategory'}
          origin={props.origin}
          size={animating ? size : props.size}
          color={{r: 107, g: 198, b: 165}}
          alpha={alpha}
          radians={radians + 0.5 * Math.PI}
          distance={animating ? distance : props.distance}
          divElementId={props.paidDivElementId}
          formElementId={props.paidFormElementId}
          hideFormElement={animating}
        />
        <IkigaiCategory
          id={props.id + '_goodCategory'}
          origin={props.origin}
          size={animating ? size : props.size}
          color={{r: 10, g: 86, b: 136}}
          alpha={alpha}
          radians={radians + Math.PI}
          distance={animating ? distance : props.distance}
          divElementId={props.goodDivElementId}
          formElementId={props.goodFormElementId}
          hideFormElement={animating}
        />
      </>
    );
  }
);
