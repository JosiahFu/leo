import './DefaultPageNav.scss';
import {Outlet} from 'react-router';
import {Layout, Menu} from 'antd';
import {
  AppstoreOutlined,
  DesktopOutlined,
  RocketOutlined,
  SettingOutlined,
  UserOutlined,
} from '@ant-design/icons';

const {Header, Sider, Content} = Layout;

enum MenuKeys {
  DASHBOARD,
  PROJECTS,
  MY_PROJECTS,
  IKIGAI_BUILDER,
  INTERNSHIPS,
  MY_ACCOUNT,
  ADMIN,
  EDIT_DISTRICTS,
  EDIT_SCHOOLS,
  EDIT_USERS,
}

export function DefaultPageNav() {
  return (
    <>
      <Layout>
        <Header>
          <div>
            <a href="/">
              <img
                src="/logo-orange-on-white.svg"
                alt="Project Leo Logo | Go Home"
              />
            </a>
          </div>
          <div>[TODO: Breadcrumbs]</div>
        </Header>
        <Layout>
          <Sider>
            <Menu mode="inline">
              <Menu.Item key={MenuKeys.DASHBOARD} icon={<AppstoreOutlined />}>
                Dashboard
              </Menu.Item>
              <Menu.SubMenu
                key={MenuKeys.PROJECTS}
                icon={<RocketOutlined />}
                title="Projects"
              >
                <Menu.Item key={MenuKeys.MY_PROJECTS}>My Projects</Menu.Item>
                <Menu.Item key={MenuKeys.IKIGAI_BUILDER}>
                  <a href="/projects/ikigai-builder">Ikigai Builder</a>
                </Menu.Item>
              </Menu.SubMenu>
              <Menu.Item key={MenuKeys.INTERNSHIPS} icon={<DesktopOutlined />}>
                Internships
              </Menu.Item>
              <Menu.Item key={MenuKeys.MY_ACCOUNT} icon={<UserOutlined />}>
                My Account
              </Menu.Item>
              <Menu.SubMenu
                key={MenuKeys.ADMIN}
                icon={<SettingOutlined />}
                title="Admin"
              >
                <Menu.Item key={MenuKeys.EDIT_DISTRICTS}>
                  <a href="/profiles/edit-districts">Edit Districts</a>
                </Menu.Item>
                <Menu.Item key={MenuKeys.EDIT_SCHOOLS}>
                  <a href="/profiles/edit-schools">Edit Schools</a>
                </Menu.Item>
                <Menu.Item key={MenuKeys.EDIT_USERS}>
                  <a href="/profiles/edit-users">Edit Users</a>
                </Menu.Item>
              </Menu.SubMenu>
            </Menu>
          </Sider>
          <Content
            style={{border: '#F0781F solid 1px', padding: 0}}
            className="content"
          >
            <Outlet />
          </Content>
        </Layout>
      </Layout>
    </>
  );
}
