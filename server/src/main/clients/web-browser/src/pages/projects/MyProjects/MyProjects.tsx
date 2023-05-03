import './MyProjects.scss';
import {List} from 'antd';
import {useEffect, useState} from 'react';
import {class_management, createService, pl_types} from '../../../protos';
import ClassManagementService = class_management.ClassManagementService;
import {getCurrentUser} from '../../../utils/authentication';
import IProject = pl_types.IProject;
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';

export function MyProjects() {
  const user = getCurrentUser();
  if (user == null) {
    return <></>;
  }

  const [projects, setProjects] = useState<IProject[]>([]);

  useEffect(() => {
    const classManagementService = createService(
      ClassManagementService,
      'ClassManagementService'
    );
    classManagementService
      .getProjects({userXId: user!.userXId!})
      .then(response => setProjects(response.projects));
  }, []);

  return (
    <>
      <DefaultPage title="My Projects">
        <div className="brief-instructions">Select a class to begin.</div>
        <List
          dataSource={projects}
          renderItem={project => (
            <List.Item itemID={project.id?.toString()}>
              NAME: {project.name}
              <br />
              DESCRIPTION: {project.shortDescr}
              <br />
              DETAILS: {project.longDescr}
              <br />
            </List.Item>
          )}
        />
      </DefaultPage>
    </>
  );
}
