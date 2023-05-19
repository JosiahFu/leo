import {ReactNode} from 'react';

function ProjectPost(props: {
  title: string;
  date: Date;
  image: string;
  content: ReactNode;
}) {
  return (
    <article>
      <div className="post-header">
        <h2>{props.title}</h2>
        <span>{props.date.toLocaleDateString()}</span>
      </div>
      <img src={props.image} alt={props.title} />
      {props.content}
    </article>
  );
}

export {ProjectPost};
