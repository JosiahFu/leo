import './IkigaiReel.scss';
import {useEffect, useState} from 'react';
import {IkigaiCategory} from '../IkigaiCategory/IkigaiCategory';
import {doTransition} from '../utils/transitions';

export type IkigaiFunctions = {
  show: (durationMs: number) => Promise<void>;
  rotateSlot(durationMs: number): Promise<void>;
  hide: (durationMs: number) => Promise<void>;
};

export type Coordinate = {
  x: number;
  y: number;
};

enum Position {
  LOVES,
  GOOD_AT,
  PAID_FOR,
  WORLD_NEEDS,
}

export function IkigaiReel(props: {
  id: string;
  // Setting this to null will hide the diagram.
  origin: Coordinate | null;
  size: number;
  sizeDelta: number;
  distance: number;
  distanceDelta: number;
}) {
  const visibleAlpha = 0.2;
  const showHideDurationMs = 750;
  const rotateDurationMs = 500;
  const pauseDurationMs = 1000;

  const [origin, setOrigin] = useState<Coordinate | null>(null);
  const [radians, setRadians] = useState(0);
  const [alpha, setAlpha] = useState(0);
  const [position, setPosition] = useState<Position | null>(null);
  const [firstTime, setFirstTime] = useState(true);

  const [distance, setDistance] = useState(0);
  const [distanceDelta, setDistanceDelta] = useState(0);
  const [size, setSize] = useState(0);
  const [sizeDelta, setSizeDelta] = useState(0);

  const [lovesHighlight, setLovesHighlight] = useState(0);
  const [worldNeedsHighlight, setWorldNeedsHighlight] = useState(0);
  const [paidForHighlight, setPaidForHighlight] = useState(0);
  const [goodAtHighlight, setGoodAtHighlight] = useState(0);

  function show(durationMs: number): Promise<void> {
    return doTransition(
      durationMs,
      {setFn: setRadians, begin: radians - 2 * Math.PI, end: 0},
      {
        setFn: setDistance,
        begin: 0,
        end: props.distance,
      },
      {
        setFn: setDistanceDelta,
        begin: 0,
        end: props.distanceDelta,
      },
      {
        setFn: setSize,
        begin: 0,
        end: props.size,
      },
      {
        setFn: setSizeDelta,
        begin: 0,
        end: props.sizeDelta,
      },
      {
        setFn: setAlpha,
        begin: 0,
        end: visibleAlpha,
      }
    ).finally(() => setPosition(Position.LOVES));
  }

  function hide(durationMs: number): Promise<void> {
    return doTransition(
      durationMs,
      {setFn: setRadians, begin: radians + 4 * Math.PI, end: 0},
      {setFn: setDistance, begin: props.distance, end: 0},
      {setFn: setAlpha, begin: visibleAlpha, end: 0},
      {setFn: setSize, begin: props.size, end: 0}
    );
  }

  useEffect(() => {
    if (position === null) {
      return;
    }

    let newPosition = position;
    let newLovesHighlight = 0;
    let newGoodAtHighlight = 0;
    let newPaidForHighlight = 0;
    let newWorldNeedsHighlight = 0;
    let newRadians = 0;

    switch (position) {
      case Position.LOVES:
        newPosition = Position.GOOD_AT;
        newGoodAtHighlight = 1;
        newRadians = 0.5 * Math.PI;
        break;
      case Position.GOOD_AT:
        newPosition = Position.PAID_FOR;
        newPaidForHighlight = 1;
        newRadians = Math.PI;
        break;
      case Position.PAID_FOR:
        newPosition = Position.WORLD_NEEDS;
        newWorldNeedsHighlight = 1;
        newRadians = 1.5 * Math.PI;
        break;
      case Position.WORLD_NEEDS:
        newPosition = Position.LOVES;
        newLovesHighlight = 1;
        newRadians = 0;
        break;
    }

    console.log('' + position + ': ' + radians + ' => ' + newRadians);

    doTransition(
      firstTime ? showHideDurationMs / 4 : rotateDurationMs,
      {
        setFn: setLovesHighlight,
        begin: lovesHighlight,
        end: newLovesHighlight,
      },
      {
        setFn: setGoodAtHighlight,
        begin: goodAtHighlight,
        end: newGoodAtHighlight,
      },
      {
        setFn: setPaidForHighlight,
        begin: paidForHighlight,
        end: newPaidForHighlight,
      },
      {
        setFn: setWorldNeedsHighlight,
        begin: worldNeedsHighlight,
        end: newWorldNeedsHighlight,
      },
      {
        setFn: setRadians,
        begin: newRadians - 0.5 * Math.PI,
        end: newRadians,
      }
    ).finally(() => {
      setFirstTime(false);
      setTimeout(() => setPosition(newPosition), pauseDurationMs);
    });
  }, [position]);

  // Reposition and show the diagram when its properties are set/changed.
  useEffect(() => {
    if (!props.origin) {
      if (origin) {
        hide(showHideDurationMs).finally(() => setOrigin(null));
      }
    } else if (!origin) {
      setOrigin(props.origin);
      show(showHideDurationMs);
    } else if (props.origin && origin) {
      const x1 = origin.x;
      const y1 = origin.y;
      const x2 = props.origin.x;
      const y2 = props.origin.y;
      doTransition(
        250,
        {
          setFn: (fraction: number) => {
            setOrigin({
              x: x1 + (x2 - x1) * fraction,
              y: y1 + (y2 - y1) * fraction,
            });
          },
          begin: 0,
          end: 1,
        },
        {setFn: setSize, begin: size, end: props.size},
        {setFn: setDistance, begin: distance, end: props.distance}
      );
    }
  }, [props.origin, props.size, props.distance]);

  return (
    <>
      <div style={{visibility: origin ? 'visible' : 'hidden'}}>
        <IkigaiCategory
          id={props.id + '.lovesCategory'}
          origin={origin || {x: 0, y: 0}}
          size={size + lovesHighlight * sizeDelta}
          color={{r: 249, g: 209, b: 98}}
          alpha={alpha}
          radians={radians - 0.5 * Math.PI}
          distance={distance + lovesHighlight * distanceDelta}
          resizeAndRotateElementIds={[
            props.id + '.lovesPanel',
            props.id + '.lovesValue',
          ]}
          highlightBackground={lovesHighlight}
        />
        <IkigaiCategory
          id={props.id + '.goodAtCategory'}
          origin={origin || {x: 0, y: 0}}
          size={size + goodAtHighlight * sizeDelta}
          color={{r: 10, g: 86, b: 136}}
          alpha={alpha}
          radians={radians - Math.PI}
          textRadians={radians - 0.5 * Math.PI}
          distance={distance + goodAtHighlight * distanceDelta}
          resizeAndRotateElementIds={[
            props.id + '.goodAtPanel',
            props.id + '.goodAtValue',
          ]}
          highlightBackground={goodAtHighlight}
        />
        <IkigaiCategory
          id={props.id + '.paidForCategory'}
          origin={origin || {x: 0, y: 0}}
          size={size + paidForHighlight * sizeDelta}
          color={{r: 107, g: 198, b: 165}}
          alpha={alpha}
          radians={radians - 1.5 * Math.PI}
          textRadians={radians - 0.5 * Math.PI}
          distance={distance + paidForHighlight * distanceDelta}
          resizeAndRotateElementIds={[
            props.id + '.paidForPanel',
            props.id + '.paidForValue',
          ]}
          highlightBackground={paidForHighlight}
        />
        <IkigaiCategory
          id={props.id + '.worldNeedsCategory'}
          origin={origin || {x: 0, y: 0}}
          size={size + worldNeedsHighlight * sizeDelta}
          color={{r: 243, g: 149, b: 79}}
          alpha={alpha}
          radians={radians}
          textRadians={radians - 0.5 * Math.PI}
          distance={distance + worldNeedsHighlight * distanceDelta}
          resizeAndRotateElementIds={[
            props.id + '.worldNeedsPanel',
            props.id + '.worldNeedsValue',
          ]}
          highlightBackground={worldNeedsHighlight}
        />
        <form>
          {/* Initially, set everything to hidden to not flash before being positioned.*/}
          <div
            id={props.id + '.lovesPanel'}
            className="rotating-panel center top"
            style={{visibility: 'hidden', opacity: 1 - lovesHighlight}}
          >
            <div className="rotating-panel-contents">
              What you <b>LOVE</b>
            </div>
          </div>
          <div
            id={props.id + '.lovesValue'}
            className="rotating-panel center top"
            style={{visibility: 'hidden', opacity: lovesHighlight}}
          >
            <div className="rotating-panel-contents">Being silly!</div>
          </div>

          <div
            id={props.id + '.worldNeedsPanel'}
            className="rotating-panel center top"
            style={{visibility: 'hidden', opacity: 1 - worldNeedsHighlight}}
          >
            <div className="rotating-panel-contents">
              What the world <b>NEEDS</b>
            </div>
          </div>
          <div
            id={props.id + '.worldNeedsValue'}
            className="rotating-panel center top"
            style={{visibility: 'hidden', opacity: worldNeedsHighlight}}
          >
            <div className="rotating-panel-contents">Entertainment!</div>
          </div>

          <div
            id={props.id + '.paidForPanel'}
            className="rotating-panel center top"
            style={{visibility: 'hidden', opacity: 1 - paidForHighlight}}
          >
            <div className="rotating-panel-contents">
              What you can be <b>PAID&nbsp;FOR</b>
            </div>
          </div>
          <div
            id={props.id + '.paidForValue'}
            className="rotating-panel center top"
            style={{visibility: 'hidden', opacity: paidForHighlight}}
          >
            <div className="rotating-panel-contents">Circus Clown!</div>
          </div>

          <div
            id={props.id + '.goodAtPanel'}
            className="rotating-panel center top"
            style={{visibility: 'hidden', opacity: 1 - goodAtHighlight}}
          >
            <div className="rotating-panel-contents">
              What you are <b>GOOD&nbsp;AT</b>
            </div>
          </div>
          <div
            id={props.id + '.goodAtValue'}
            className="rotating-panel center top"
            style={{visibility: 'hidden', opacity: goodAtHighlight}}
          >
            <div className="rotating-panel-contents">Juggling!</div>
          </div>
        </form>
      </div>
    </>
  );
}
