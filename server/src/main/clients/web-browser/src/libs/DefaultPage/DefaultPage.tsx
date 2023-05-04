import './DefaultPage.scss';

import {getCurrentUser} from '../../utils/authentication';
import {
  BellOutlined,
  QuestionCircleOutlined,
  SmileTwoTone,
} from '@ant-design/icons';
import {PropsWithChildren} from 'react';

export function DefaultPage(props: PropsWithChildren<{title: string}>) {
  const user = getCurrentUser(() => {});

  return (
    <>
      <div className="page">
        <div className="page-header">
          <div className="page-title">{props.title}</div>
          <div className="page-account">
            <QuestionCircleOutlined />
            <BellOutlined />
            <span className="profile-icon">
              <SmileTwoTone />
            </span>
            {user?.firstName} {user?.lastName}
          </div>
        </div>
        <div className="page-body">{props.children}</div>
      </div>
    </>
  );
}
