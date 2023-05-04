import './MyProjects.scss';
import {useEffect, useState} from 'react';
import {class_management, createService, pl_types} from '../../../protos';
import ClassManagementService = class_management.ClassManagementService;
import {getCurrentUser} from '../../../utils/authentication';
import IProject = pl_types.IProject;
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {ProjectCard} from '../../../libs/ProjectCard/ProjectCard';

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
        <div>
          {projects.map(project => (
            <ProjectCard key={project.id} project={project} />
          ))}
        </div>
      </DefaultPage>
    </>
  );
}
