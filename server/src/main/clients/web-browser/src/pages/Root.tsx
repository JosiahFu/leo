import {useEffect, useState} from 'react';
import * as protos from '../protos';
import {Coordinate, IkigaiReel} from '../IkigaiReel/IkigaiReel';
import './Root.scss';
import {Link} from 'react-router-dom';

export function Root() {
  const [messageOfTheDay, setMessageOfTheDay] = useState('');

  const [ikigaiCenter, setIkigaiCenter] = useState<Coordinate | null>(null);
  const [ikigaiDistance, setIkigaiDistance] = useState(0);
  const [ikigaiSize, setIkigaiSize] = useState(0);

  function updateIkigaiPosition() {
    const main = document.getElementsByClassName('main').item(0) as HTMLElement;
    if (main) {
      setIkigaiCenter({
        x: main.offsetLeft + main.offsetWidth / 2,
        y:
          main.offsetTop +
          main.offsetHeight / 2 +
          (Math.min(main.offsetWidth, main.offsetHeight) / 2) * 0.07,
      });
      setIkigaiSize((Math.min(main.offsetWidth, main.offsetHeight) / 2) * 0.8);
      setIkigaiDistance(
        (Math.min(main.offsetWidth, main.offsetHeight) / 2) * 0.8 * 0.4
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
        setMessageOfTheDay(response.message!);
      })
      .catch((error: Error) => {
        setMessageOfTheDay(`${error.name}: ${error.message}`);
      });
  }, []);

  return (
    <>
      <Link to="edit-districts">Edit Districts</Link>
      <br />
      <Link to="edit-schools">Edit Schools</Link>
      <br />
      <Link to="create-user">Create User</Link>
      <br />
      <Link to="login">Login</Link>
      <br />
      <Link to="student/project-gen">Project Idea Generation Prototype</Link>
      <IkigaiReel
        id="IkigaiReel"
        origin={ikigaiCenter}
        size={ikigaiSize}
        sizeDelta={ikigaiSize / 6}
        distance={ikigaiDistance * 1.1}
        distanceDelta={ikigaiDistance / 6}
      />
      {messageOfTheDay}
    </>
  );
}
