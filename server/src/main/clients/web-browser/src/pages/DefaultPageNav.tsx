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
import {getCurrentUser, logout, Role} from '../utils/authentication';
import {Link} from 'react-router-dom';

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
  const user = getCurrentUser(() => {});

  return (
    <>
      <Layout>
        <Header>
          <div>
            <Link to="/">
              <img
                src="/logo-orange-on-white.svg"
                alt="Project Leo Logo | Go Home"
              />
            </Link>
          </div>
          <div style={{whiteSpace: 'nowrap'}}>[TODO: Breadcrumbs]</div>
          <div style={{width: '100%', padding: 0, textAlign: 'right'}}>
            <span
              style={{
                display: user != null ? 'inline' : 'none',
              }}
              onClick={() => {
                logout();
                window.location.reload();
              }}
            >
              <Link to="/login">Logout</Link>
            </span>
            <span
              style={{
                display: user == null ? 'inline' : 'none',
              }}
            >
              <Link to="/login">Login</Link>
            </span>
          </div>
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
                  <Link to="/projects/ikigai-builder">Ikigai Builder</Link>
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
                style={{
                  display:
                    user != null && user.roles.has(Role.ADMIN)
                      ? 'block'
                      : 'none',
                }}
              >
                <Menu.Item key={MenuKeys.EDIT_DISTRICTS}>
                  <Link to="/profiles/edit-districts">Edit Districts</Link>
                </Menu.Item>
                <Menu.Item key={MenuKeys.EDIT_SCHOOLS}>
                  <Link to="/profiles/edit-schools">Edit Schools</Link>
                </Menu.Item>
                <Menu.Item key={MenuKeys.EDIT_USERS}>
                  <Link to="/profiles/edit-users">Edit Users</Link>
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
