import '../../DefaultPageNav.scss';
import './IkigaiBuilder.scss';
import {Layout} from 'antd';

const {Sider, Content} = Layout;

export function IkigaiBuilder() {
  return (
    <>
      <Layout style={{height: '100%'}}>
        <Content style={{borderRight: '#F0781F solid 1px'}}>
          <div className="subtitle">Ikigai Builder</div>
          <div className="instructions">Click each circle to edit:</div>
        </Content>
        <Sider reverseArrow>
          <div>Saved Projects</div>
        </Sider>
      </Layout>
    </>
  );
}
