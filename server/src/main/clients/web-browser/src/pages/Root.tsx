import {useEffect, useState} from 'react';
import * as protos from '../protos';
import {Coordinate, Ikigai} from '../Ikigai/Ikigai';
import './Root.scss';
import {Link} from 'react-router-dom';

export function Root() {
  // motd = MessageOfTheDay
  const [motd, setMotd] = useState('');

  const [ikigaiCenter, setIkigaiCenter] = useState<Coordinate | null>(null);
  const [ikigaiDistance, setIkigaiDistance] = useState(0);
  const [ikigaiSize, setIkigaiSize] = useState(0);

  function updateIkigaiPosition() {
    const main = document.getElementsByClassName('main').item(0) as HTMLElement;
    if (main) {
      setIkigaiCenter({
        x: main.offsetLeft + main.offsetWidth / 2,
        y: main.offsetTop + main.offsetHeight / 2,
      });
      setIkigaiSize((Math.min(main.offsetWidth, main.offsetHeight) / 2) * 0.9);
      setIkigaiDistance(
        (Math.min(main.offsetWidth, main.offsetHeight) / 4) * 0.9
      );
    }
  }

  // Updates the Ikigai position after layout and on window resize.
  useEffect(() => {
    updateIkigaiPosition();
    window.addEventListener('resize', updateIkigaiPosition);
  }, []);

  // Loads and sets the message of the day.
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
        />
      </div>
      <div className="footer">{motd}</div>
    </>
  );
}
