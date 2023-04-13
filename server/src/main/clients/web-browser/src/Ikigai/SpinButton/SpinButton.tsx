import './SpinButton.scss';
import {forwardRef, Ref, useEffect, useImperativeHandle, useState} from 'react';
import {doTransition} from '../../utils/transitions';

export type SpinButtonFunctions = {
  show: (durationMs: number) => Promise<void>;
  hide: (durationMs: number) => Promise<void>;
};

export const SpinButton = forwardRef(
  (
    props: {
      id: string;
      origin: {x: number; y: number};
      diameter: number;
      enabled: boolean;
      onClick: () => void;
    },
    ref: Ref<SpinButtonFunctions>
  ) => {
    const [diameter, setDiameter] = useState(0);
    const [visible, setVisible] = useState(false);
    const [disabled, setDisabled] = useState(false);
    const [radians, setRadians] = useState(0);

    const functions = {
      show(durationMs: number): Promise<void> {
        setDisabled(true);
        setVisible(true);
        return doTransition(
          durationMs,
          {
            setFn: setDiameter,
            begin: 0,
            end: props.diameter,
          },
          {setFn: setRadians, begin: 4 * Math.PI, end: 0}
        ).finally(() => setDisabled(false));
      },

      hide(durationMs: number): Promise<void> {
        return doTransition(
          durationMs,
          {
            setFn: setDiameter,
            begin: props.diameter,
            end: 0,
          },
          {setFn: setRadians, begin: 4 * Math.PI, end: 0}
        ).finally(() => setVisible(false));
      },
    };
    useImperativeHandle(ref, () => functions);

    useEffect(() => {
      if (visible) {
        doTransition(250, {
          setFn: setDiameter,
          begin: diameter,
          end: props.diameter,
        });
      }
    }, [props.diameter]);

    function onClick() {
      if (!disabled && props.enabled) {
        props.onClick();
      }
    }

    return (
      <>
        <div
          id={props.id}
          className="spin-button"
          style={{
            left: props.origin.x - diameter / 2,
            top: props.origin.y - diameter / 2,
            width: diameter,
            height: diameter,
            fontSize: diameter / 4,
            visibility: visible ? 'visible' : 'hidden',
            transform: `rotate(${radians}rad)`,
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
