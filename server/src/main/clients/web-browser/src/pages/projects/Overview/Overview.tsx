import './Overview.scss';
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {ProjectDashboard} from '../../../libs/projects/ProjectDashboard/ProjectDashboard';
import {Layout} from 'antd';

export function Overview() {
  return (
    <>
      <DefaultPage title="Projects Overview">
        <Layout style={{height: '100%'}}>
          <Layout.Content
            style={{borderRight: '#F0781F solid 1px', padding: '0.5em'}}
          >
            <div style={{width: '100%', height: '100%'}}>
              <ProjectDashboard />
            </div>
          </Layout.Content>
        </Layout>
      </DefaultPage>
    </>
  );
}
