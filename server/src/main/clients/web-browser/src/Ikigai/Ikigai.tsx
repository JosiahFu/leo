import './Ikigai.scss';
import {
  forwardRef,
  Ref,
  useEffect,
  useImperativeHandle,
  useRef,
  useState,
} from 'react';
import {IkigaiCategory} from '../IkigaiCategory/IkigaiCategory';
import {doTransition, overshootTransition} from '../utils/transitions';
import {SpinButton, SpinIconFunctions} from '../SpinIcon/SpinButton';
import {PartialTextOpenAiPrompt} from '../PartialTextOpenAiPrompt/PartialTextOpenAiPrompt';
import {partial_text_openai_prompt} from '../protos/protobuf-js';
import GetSuggestionsRequest = partial_text_openai_prompt.GetSuggestionsRequest;

export type IkigaiFunctions = {
  show: (durationMs: number) => Promise<void>;
  hide: (durationMs: number) => Promise<void>;
};

export type Coordinate = {
  x: number;
  y: number;
};

export const Ikigai = forwardRef(
  (
    props: {
      id: string;
      // Setting this to null will hide the diagram.
      origin: Coordinate | null;
      size: number;
      distance: number;
    },
    ref: Ref<IkigaiFunctions>
  ) => {
    const visibleAlpha = 0.2;
    const durationMs = 750;

    const [origin, setOrigin] = useState<Coordinate | null>(null);
    const [animating, setAnimating] = useState(true);
    const [radians, setRadians] = useState(0);
    const [distance, setDistance] = useState(0);
    const [alpha, setAlpha] = useState(0);
    const [size, setSize] = useState(0);

    const [lovesValueIsSet] = useState(false);
    const [worldNeedsValueIsSet] = useState(false);
    const [paidForValueIsSet] = useState(false);
    const [goodAtValueIsSet] = useState(false);

    const spinIcon = useRef<SpinIconFunctions>(null);
    const ikigai = {
      show(durationMs: number): Promise<void> {
        setAnimating(true);
        let promise = doTransition(
          durationMs,
          {setFn: setRadians, begin: radians + 2 * Math.PI, end: 0},
          {
            setFn: setDistance,
            begin: 0,
            end: props.distance,
          },
          {
            setFn: setAlpha,
            begin: 0,
            end: visibleAlpha,
          },
          {
            setFn: setSize,
            begin: 0,
            end: props.size,
            fractionFn: overshootTransition(1.1, 0.9),
          }
        ) as Promise<unknown>;

        if (spinIcon.current) {
          promise = Promise.all([promise, spinIcon.current!.show(durationMs)]);
        }

        return promise.finally(() => setAnimating(false)) as Promise<void>;
      },

      hide(durationMs: number): Promise<void> {
        setAnimating(true);
        let promise = doTransition(
          durationMs,
          {setFn: setRadians, begin: radians + 4 * Math.PI, end: 0},
          {setFn: setDistance, begin: props.distance, end: 0},
          {setFn: setAlpha, begin: visibleAlpha, end: 0},
          {setFn: setSize, begin: props.size, end: 0}
        ) as Promise<unknown>;

        if (spinIcon.current) {
          promise = Promise.all([promise, spinIcon.current!.hide(durationMs)]);
        }
        return promise.finally(() => setAnimating(false)) as Promise<void>;
      },
    };
    useImperativeHandle(ref, () => ikigai);

    // Reposition and show the diagram when its properties are set/changed.
    useEffect(() => {
      if (!props.origin) {
        if (origin) {
          ikigai.hide(durationMs).finally(() => setOrigin(null));
        }
      } else if (!origin) {
        setOrigin(props.origin);
        ikigai.show(durationMs);
      } else {
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
            size={size}
            color={{r: 249, g: 209, b: 98}}
            alpha={alpha}
            radians={radians + 1.5 * Math.PI}
            distance={distance}
            resizeAndRotateElementIds={[props.id + '.lovesPanel']}
            highlightBackground={lovesValueIsSet ? 1 : 0}
          />
          <IkigaiCategory
            id={props.id + '.worldNeedsCategory'}
            origin={origin || {x: 0, y: 0}}
            size={size}
            color={{r: 243, g: 149, b: 79}}
            alpha={alpha}
            radians={radians + 0 * Math.PI}
            distance={distance}
            resizeAndRotateElementIds={[props.id + '.worldNeedsPanel']}
            highlightBackground={worldNeedsValueIsSet ? 1 : 0}
          />
          <IkigaiCategory
            id={props.id + '.paidForCategory'}
            origin={origin || {x: 0, y: 0}}
            size={size}
            color={{r: 107, g: 198, b: 165}}
            alpha={alpha}
            radians={radians + 0.5 * Math.PI}
            distance={distance}
            resizeAndRotateElementIds={[props.id + '.paidForPanel']}
            highlightBackground={paidForValueIsSet ? 1 : 0}
          />
          <IkigaiCategory
            id={props.id + '.goodAtCategory'}
            origin={origin || {x: 0, y: 0}}
            size={size}
            color={{r: 10, g: 86, b: 136}}
            alpha={alpha}
            radians={radians + Math.PI}
            distance={distance}
            resizeAndRotateElementIds={[props.id + '.goodAtPanel']}
            highlightBackground={goodAtValueIsSet ? 1 : 0}
          />
          <form>
            {/* Initially, set everything to hidden to not flash before being positioned.*/}
            <div
              id={props.id + '.lovesPanel'}
              className="rotating-panel center top"
              style={{visibility: 'hidden'}}
            >
              <div className="rotating-panel-contents">
                What you <b>LOVE</b>
                <div
                  id={props.id + '.lovesFormElements'}
                  className="form-elements-panel"
                  style={{visibility: animating ? 'hidden' : 'visible'}}
                >
                  <PartialTextOpenAiPrompt
                    id={props.id + '.lovesFormElement'}
                    initialSuggestions={[
                      'Acting in a Play',
                      'Building a Robot',
                      'Building a Website',
                      'Creating a Graphic Novel',
                      'Designing a Fashion Line',
                      'Designing a Game App',
                      'Encouraging Others',
                      'Learning About Historical Events',
                      'Learning Martial Arts',
                      'Organizing a Fundraiser',
                      'Painting a Mural',
                      'Participating in a Book Club',
                      'Participating in a Debate Competition',
                      'Planting a Community Garden',
                      'Playing an Instrument',
                      'Reading a Good Book',
                      'Solving Challenging Math Problems',
                      'Competing in Water Polo',
                      'Writing Poetry',
                      'Writing a Journalism Piece',
                    ]}
                    prompt={
                      GetSuggestionsRequest.Prompt.SUGGEST_THINGS_YOU_LOVE
                    }
                  />
                </div>
              </div>
            </div>

            <div
              id={props.id + '.worldNeedsPanel'}
              className="rotating-panel right middle"
              style={{visibility: 'hidden'}}
            >
              <div className="rotating-panel-contents">
                What the world <b>NEEDS</b>
                <div
                  id={props.id + '.worldNeedsFormElements'}
                  className="form-elements-panel"
                >
                  <select
                    id={props.id + '.worldNeedsFormElement'}
                    style={{visibility: animating ? 'hidden' : 'visible'}}
                    className="form-element"
                  >
                    <option>Select</option>
                    <option>Software Engineer</option>
                    <option>Computer Engineer</option>
                    <option>Professional Athlete</option>
                    <option>Front-End Developer</option>
                    <option>Web Designer</option>
                    <option>UX Designer</option>
                    <option>Clinical Researcher</option>
                  </select>
                </div>
              </div>
            </div>

            <div
              id={props.id + '.paidForPanel'}
              className="rotating-panel center bottom"
              style={{visibility: 'hidden'}}
            >
              <div className="rotating-panel-contents">
                What you can be <b>PAID&nbsp;FOR</b>
                <div
                  id={props.id + '.paidForFormElements'}
                  className="form-elements-panel"
                >
                  <select
                    id={props.id + '.paidForFormElement'}
                    style={{visibility: animating ? 'hidden' : 'visible'}}
                    className="form-element"
                  >
                    <option>Select</option>
                    <option>Software Engineer</option>
                    <option>Computer Engineer</option>
                    <option>Professional Athlete</option>
                    <option>Front-End Developer</option>
                    <option>Web Designer</option>
                    <option>UX Designer</option>
                    <option>Clinical Researcher</option>
                  </select>
                </div>
              </div>
            </div>

            <div
              id={props.id + '.goodAtPanel'}
              className="rotating-panel left middle"
              style={{visibility: 'hidden'}}
            >
              <div className="rotating-panel-contents">
                What you are <b>GOOD&nbsp;AT</b>
                <div
                  id={props.id + '.goodAtFormElements'}
                  className="form-elements-panel"
                >
                  <select
                    id={props.id + '.goodAtFormElement'}
                    style={{visibility: animating ? 'hidden' : 'visible'}}
                    className="form-element"
                  >
                    <option>Select</option>
                    <option>Skill 1</option>
                    <option>Skill 2</option>
                    <option>Skill 3</option>
                    <option>Skill 4</option>
                    <option>Skill 5</option>
                    <option>Skill 6</option>
                    <option>Skill 7</option>
                    <option>Skill 8</option>
                    <option>Skill 9</option>
                    <option>Skill 10</option>
                  </select>
                </div>
              </div>
            </div>
          </form>
          <SpinButton
            id={props.id + '.spinIcon'}
            origin={origin || {x: 0, y: 0}}
            size={props.size / 4}
            enabled={true}
            ref={spinIcon}
            onClick={(): Promise<void> => {
              // Eventually we will do something here.
              return ikigai
                .hide(durationMs)
                .finally(() => ikigai.show(durationMs));
            }}
          />
        </div>
      </>
    );
  }
);
