import '../../DefaultPageNav.scss';
import './MyProjects.scss';
import {Layout, List} from 'antd';
import {useEffect, useState} from 'react';
import {class_management, createService} from '../../../protos';
import ClassManagementService = class_management.ClassManagementService;
import {getCurrentUser} from '../../../utils/authentication';
import IProject = class_management.IProject;

const {Sider, Content} = Layout;

export function MyProjects() {
  const [user] = useState(
    getCurrentUser(() => {
      window.open('/login');
    })
  );

  const [projects, setProjects] = useState<IProject[]>([]);

  useEffect(() => {
    const classManagementService = createService(
      ClassManagementService,
      'ClassManagementService'
    );
    classManagementService
      .getProjects({userId: user!.userId!})
      .then(response => setProjects(response.projects));
  }, []);

  return (
    <>
      <Layout style={{height: '100%'}}>
        <Content style={{borderRight: '#F0781F solid 1px', padding: '0.5em'}}>
          <div className="subtitle">My Projects</div>
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
        </Content>
        <Sider reverseArrow style={{padding: '0.5em'}}>
          <div>[TODO: Saved Projects]</div>
        </Sider>
      </Layout>
    </>
  );
}
