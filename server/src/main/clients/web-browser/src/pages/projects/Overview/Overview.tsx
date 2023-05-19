import './Overview.scss';
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {ProjectDashboard} from '../../../libs/projects/ProjectDashboard/ProjectDashboard';
import {Layout} from 'antd';
import {Content} from 'antd/es/layout/layout';

export function Overview() {
  return (
    <>
      <DefaultPage title="My Projects">
        <Layout style={{height: '100%'}}>
          <Content style={{borderRight: '#F0781F solid 1px', padding: '0.5em'}}>
            <div style={{width: '100%', height: '100%'}}>
              <ProjectDashboard />
            </div>
          </Content>
        </Layout>
      </DefaultPage>
    </>
  );
}
