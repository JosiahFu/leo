import './ProjectCard.scss';

import {Card, Modal} from 'antd';
import {ExpandOutlined} from '@ant-design/icons';
import {pl_types} from '../../generated/protobuf-js';
import IProject = pl_types.IProject;
import {useState} from 'react';

export function ProjectCard(props: {project: IProject}) {
  const [expanded, setExpanded] = useState(false);

  return (
    <>
      <Card
        title={props.project.name}
        extra={<ExpandOutlined onClick={() => setExpanded(!expanded)} />}
      >
        {props.project.shortDescr}
      </Card>
      <Modal
        open={expanded}
        closable={true}
        footer={
          <button
            className="ant-btn ant-btn-primary"
            onClick={() => setExpanded(false)}
          >
            OK
          </button>
        }
        onCancel={() => setExpanded(false)}
        onOk={() => setExpanded(false)}
      >
        <div style={{fontWeight: 'bold', width: '100%'}}>
          {props.project.name}
        </div>
        <div
          style={{fontWeight: 'lighter', fontStyle: 'italic', width: '100%'}}
        >
          {props.project.shortDescr}
        </div>
        <div
          style={{fontStyle: 'small', width: '100%', whiteSpace: 'pre-wrap'}}
        >
          {props.project.longDescr}
        </div>
      </Modal>
    </>
  );
}
