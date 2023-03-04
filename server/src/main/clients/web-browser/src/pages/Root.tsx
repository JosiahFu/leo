import {useState} from 'react';
import {Link} from 'react-router-dom';
import * as protos from '../protos';

function App() {
  // motd = MessageOfTheDay
  const [motd, setMotd] = useState('Loading...');

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
      setMotd(JSON.stringify(error));
    });

  return (
    <>
      <h2>Project Leo</h2>
      <Link to={'student/project-gen'}>Project Idea Generation</Link>
      <br />
      {motd}
    </>
  );
}

export default App;
