import './PageHeader.scss';

import {getCurrentUser} from '../../utils/authentication';
import {
  BellOutlined,
  QuestionCircleOutlined,
  SmileTwoTone,
} from '@ant-design/icons';

export function PageHeader(props: {title: string}) {
  const user = getCurrentUser(() => {});

  return (
    <>
      <div className="page-header">
        <QuestionCircleOutlined />
        <BellOutlined />
        <span className="profile-icon">
          <SmileTwoTone />
        </span>
        {user?.firstName} {user?.lastName}
      </div>
      <div className="page-title">{props.title}</div>
    </>
  );
}
