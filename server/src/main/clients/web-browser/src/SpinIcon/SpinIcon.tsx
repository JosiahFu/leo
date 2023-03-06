import './SpinIcon.scss';
import {forwardRef, Ref, useImperativeHandle, useState} from 'react';
import {
  doTransition,
  lateTransition,
  overshootTransition,
} from '../utils/transitions';

export type SpinIconFunctions = {
  show: (durationMs: number) => Promise<void>;
  hide: (durationMs: number) => Promise<void>;
};

export const SpinIcon = forwardRef(
  (
    props: {
      origin: {x: number; y: number};
      size: number;
      enabled: boolean;
      onClick: () => void;
    },
    ref: Ref<SpinIconFunctions>
  ) => {
    const [size, setSize] = useState(0);

    const functions = {
      show(durationMs: number): Promise<void> {
        return doTransition(durationMs, {
          setFn: setSize,
          start: 0,
          end: props.size,
          fractionFn: overshootTransition(1.2, 0.8),
        });
      },

      hide(durationMs: number): Promise<void> {
        return doTransition(durationMs, {
          setFn: setSize,
          start: props.size,
          end: 0,
          fractionFn: lateTransition(0.8),
        });
      },
    };
    useImperativeHandle(ref, () => functions);

    return (
      <>
        <div
          className="spin-icon"
          style={{
            left: props.origin.x - size / 2,
            top: props.origin.y - size / 2,
            width: size,
            height: size,
            fontSize: (size / 4).toString() + 'px',
          }}
          onClick={props.onClick}
        >
          <div style={{textAlign: 'center', width: '100%'}}>SPIN</div>
        </div>
      </>
    );
  }
);
