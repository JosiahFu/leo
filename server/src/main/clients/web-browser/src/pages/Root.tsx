import {useEffect, useRef, useState} from 'react';
import * as protos from '../protos';
import {Ikigai, IkigaiFunctions} from '../Ikigai/Ikigai';
import './Root.scss';
import {SpinIcon, SpinIconFunctions} from '../SpinIcon/SpinIcon';
import {Link} from 'react-router-dom';

enum InitializationState {
  PREPARATION,
  UNINITIALIZED,
  INITIALIZED,
  DONE,
}
let initializationState = InitializationState.PREPARATION;

export function Root() {
  const durationMs = 750;

  // motd = MessageOfTheDay
  const [motd, setMotd] = useState('Loading...');

  const ikigai = useRef<IkigaiFunctions>(null);
  const [ikigaiCenter, setIkigaiCenter] = useState({x: 0, y: 0});
  const [ikigaiDistance, setIkigaiDistance] = useState(0);
  const [ikigaiSize, setIkigaiSize] = useState(0);

  const spinIcon = useRef<SpinIconFunctions>(null);

  useEffect(() => {
    const motdService = protos.createService(
      protos.message_of_the_day.MessageOfTheDayService,
      'MessageOfTheDayService'
    );

    motdService
      .getMessage(protos.message_of_the_day.MessageRequest.create({}))
      .then((response: protos.message_of_the_day.MessageResponse) => {
        setMotd(response.message!);
      })
      .catch((error: Error) => {
        setMotd(`${error.name}: ${error.message}`);
      });
  }, []);

  useEffect(() => {
    switch (initializationState) {
      case InitializationState.PREPARATION:
        {
          initializationState = InitializationState.UNINITIALIZED;
        }
        break;

      case InitializationState.UNINITIALIZED:
        {
          const main = document
            .getElementsByClassName('main')
            .item(0) as HTMLElement;
          setIkigaiCenter({
            x: main.offsetLeft + main.offsetWidth / 2,
            y: main.offsetTop + main.offsetHeight / 2,
          });
          setIkigaiSize(Math.min(main.offsetWidth, main.offsetHeight) / 2);
          setIkigaiDistance(
            (Math.min(main.offsetWidth, main.offsetHeight) / 4) * 0.9
          );
          initializationState = InitializationState.INITIALIZED;
        }
        break;

      case InitializationState.INITIALIZED:
        {
          ikigai.current!.show(durationMs);
          spinIcon.current!.show(durationMs);
          initializationState = InitializationState.DONE;
        }
        break;

      case InitializationState.DONE:
      default:
      // Do nothing.
    }
  }, [initializationState]);

  return (
    <>
      <div className="header">PROJECT LEO</div>
      <div className="main">
        <Link to={'student/project-gen'}>
          Project Idea Generation Prototype
        </Link>
        <Ikigai
          id="ikigai"
          origin={ikigaiCenter}
          size={ikigaiSize}
          distance={ikigaiDistance}
          loveDivElementId="ikigaiLoveDivElementId"
          needsDivElementId="ikigaiNeedsDivElementId"
          paidDivElementId="ikigaiPaidDivElementId"
          goodDivElementId="ikigaiGoodDivElementId"
          loveFormElementId="ikigaiLoveFormElementId"
          needsFormElementId="ikigaiNeedsFormElementId"
          paidFormElementId="ikigaiPaidFormElementId"
          goodFormElementId="ikigaiGoodFormElementId"
          ref={ikigai}
        />
        <form style={{left: 10000}}>
          <div
            id="ikigaiLoveDivElementId"
            className="outer-form-element-div center"
          >
            <div className="inner-form-element-div">
              What you <b>LOVE</b>
              <select id="ikigaiLoveFormElementId" className="form-element">
                <option>Select</option>
                <option>Engineering</option>
                <option>Building</option>
                <option>Designing</option>
                <option>Tinkering</option>
                <option>Software Design</option>
                <option>Gardening</option>
                <option>Cooking</option>
                <option>Programming</option>
                <option>Web Design</option>
                <option>Marketing</option>
              </select>
            </div>
          </div>

          <div
            id="ikigaiNeedsDivElementId"
            className="outer-form-element-div right middle"
          >
            <div className="inner-form-element-div">
              What the world <b>NEEDS</b>
              <select id="ikigaiNeedsFormElementId" className="form-element">
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

          <div
            id="ikigaiPaidDivElementId"
            className="outer-form-element-div center bottom"
          >
            <div className="inner-form-element-div">
              What you can be <b>PAID&nbsp;FOR</b>
              <select id="ikigaiPaidFormElementId" className="form-element">
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

          <div
            id="ikigaiGoodDivElementId"
            className="outer-form-element-div middle"
          >
            <div className="inner-form-element-div">
              What you are <b>GOOD&nbsp;AT</b>
              <select id="ikigaiGoodFormElementId" className="form-element">
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
        </form>

        <SpinIcon
          origin={ikigaiCenter}
          size={ikigaiSize / 4}
          enabled={false}
          onClick={() => {
            spinIcon.current!.hide(durationMs);
            ikigai.current!.hide(durationMs);
          }}
          ref={spinIcon}
        />
      </div>
      <div className="footer">{motd}</div>
    </>
  );
}
