import {PropsWithChildren} from 'react';

function ProjectPost(
  props: PropsWithChildren<{title: string; date: Date; image: string}>
) {
  return (
    <article>
      <div className="post-header">
        <h2>{props.title}</h2>
        <span>{props.date.toLocaleDateString()}</span>
      </div>
      <img src={props.image} alt={props.title} />
      {props.children}
    </article>
  );
}

export {ProjectPost};
