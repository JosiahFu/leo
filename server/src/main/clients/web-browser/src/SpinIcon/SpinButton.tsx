import './SpinButton.scss';
import {forwardRef, Ref, useEffect, useImperativeHandle, useState} from 'react';
import {
  doTransition,
  lateTransition,
  overshootTransition,
} from '../utils/transitions';

export type SpinIconFunctions = {
  show: (durationMs: number) => Promise<void>;
  hide: (durationMs: number) => Promise<void>;
};

export const SpinButton = forwardRef(
  (
    props: {
      id: string;
      origin: {x: number; y: number};
      size: number;
      enabled: boolean;
      onClick: () => Promise<void>;
    },
    ref: Ref<SpinIconFunctions>
  ) => {
    const [size, setSize] = useState(0);
    const [visible, setVisible] = useState(false);
    const [disabled, setDisabled] = useState(false);

    const functions = {
      show(durationMs: number): Promise<void> {
        setVisible(true);
        return doTransition(durationMs, {
          setFn: setSize,
          begin: 0,
          end: props.size,
          fractionFn: overshootTransition(1.2, 0.8),
        }).finally(() => setVisible(true));
      },

      hide(durationMs: number): Promise<void> {
        return doTransition(durationMs, {
          setFn: setSize,
          begin: props.size,
          end: 0,
          fractionFn: lateTransition(0.8),
        }).finally(() => setVisible(false));
      },
    };
    useImperativeHandle(ref, () => functions);

    useEffect(() => {
      if (visible) {
        doTransition(250, {
          setFn: setSize,
          begin: size,
          end: props.size,
        });
      }
    }, [props.size]);

    function onClick() {
      if (!disabled) {
        setDisabled(true);
        props.onClick().finally(() => setDisabled(false));
      }
    }

    return (
      <>
        <div
          id={props.id}
          className="spin-button"
          style={{
            left: props.origin.x - size / 2,
            top: props.origin.y - size / 2,
            width: size,
            height: size,
            fontSize: size / 4,
            visibility: visible ? 'visible' : 'hidden',
          }}
          role="button"
          onClick={onClick}
        >
          SPIN
        </div>
      </>
    );
  }
);
